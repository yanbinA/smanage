package com.temple.manage.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.domain.dto.ImproveDto;
import com.temple.manage.entity.Improve;
import com.temple.manage.entity.ImproveProcess;
import com.temple.manage.entity.ImproveType;
import com.temple.manage.entity.enums.ImproveProcessEnum;
import com.temple.manage.entity.enums.ImproveStatusEnum;
import com.temple.manage.entity.enums.ImproverLevelEnum;
import com.temple.manage.mapper.ImproveMapper;
import com.temple.manage.security.SecurityUtils;
import com.temple.manage.service.ImproveService;
import com.temple.manage.service.ImproveTypeService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author messi
* @description 针对表【s_improve】的数据库操作Service实现
* @createDate 2022-01-10 21:05:05
*/
@Service
@Slf4j
public class ImproveServiceImpl extends ServiceImpl<ImproveMapper, Improve>
    implements ImproveService{
    private final WxCpService wxCpService;
    private final ImproveTypeService improveTypeService;

    public ImproveServiceImpl(ImproveTypeService improveTypeService,
                             WxCpService wxCpService) {
        this.improveTypeService = improveTypeService;
        this.wxCpService = wxCpService;
    }
    /**
     * @param improveDto ImproveDto
     */
    @Override
    public void submit(ImproveDto improveDto) throws WxErrorException {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        Improve improve = new Improve();
        Integer improveTypeId = improveDto.getImproveTypeId();
        BeanUtils.copyProperties(improveDto, improve);


        improve.setImproveTypeId(improveTypeId);

        improve.setStatus(ImproveStatusEnum.IN_APPROVAL);
        //设置用户信息
        String userId = username.orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        improve.setUserId(userId);
        WxCpUser wxCpUser = Optional.ofNullable(wxCpService.getUserService().getById(userId))
                .orElseThrow(() -> Asserts.throwException("获取企业微信用户数据为NULL:" + userId));
        improve.setUserName(wxCpUser.getName());
        Long[] departIds = wxCpUser.getDepartIds();
        if (departIds == null || departIds.length == 0) {
            Asserts.fail("企业微信用户没有设置部门");
        }
        improve.setDepartment(departIds[0]);
        String adoptUserId = improveDto.getAdoptUserId();
        List<ImproveProcess> processList = new ArrayList<>();
        processList.add(new ImproveProcess(improve.getUserId(), improve.getUserName(), ImproveProcessEnum.SUBMITTED, LocalDateTime.now(), null, null));
        WxCpUser adoptUser = Optional.ofNullable(wxCpService.getUserService().getById(adoptUserId))
                .orElseThrow(() -> Asserts.throwException("获取企业微信用户数据为NULL:" + adoptUserId));
        processList.add(new ImproveProcess(adoptUser.getUserId(), adoptUser.getName(), ImproveProcessEnum.IN_APPROVAL, null, null, null));
        if (improveTypeId != null) {
            ImproveType improveType = this.improveTypeService.getById(improveTypeId);
            if (improveType == null) {
                Asserts.fail("改善类型不存在,刷新后重新选择");
            }
            improve.setImproveName(improveType.getName());
            improve.setDepartmentType(improveType.getDepartmentType());
            processList.add(new ImproveProcess(improveType.getUserId(), improveType.getUserName(), ImproveProcessEnum.IN_APPROVAL, null, null, null));
        }

        improve.setProcess(processList);
        //通知审批人
        improve.setNextUserId(adoptUser.getUserId());
        improve.setNextUserName(adoptUser.getName());
        this.sendMiniNotice(adoptUser.getUserId(), "有建议需要审批", "点击查看详情");
        this.save(improve);
    }
    /**
     * @description <p>
     *     审批合理建议
     *     1.
     * </p>
     * @author messi
     * @date 2022-01-10 22:15
     * @param improveDto ImproveDto
     */
    @Override
    public void adopt(ImproveDto improveDto) throws WxErrorException {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        Improve improve = this.getById(improveDto.getId());
        if (improve.getStatus() != ImproveStatusEnum.IN_APPROVAL) {
            Asserts.fail("审批流程已完成");
        }
        if (!improve.getNextUserId().equals(userId)) {
            Asserts.fail("无审批权限");
        }
        if (improveDto.getImproveTypeId() != null) {
            if (improve.getImproveTypeId() != null && improve.getImproveTypeId().intValue() != improveDto.getImproveTypeId()) {
                if (improve.getProcess().get(1).getOperation() != ImproveProcessEnum.IN_APPROVAL) {
                    Asserts.fail("改善类型已确定,无法修改");
                }
            }
            Integer improveTypeId = improveDto.getImproveTypeId();
            ImproveType improveType = this.improveTypeService.getById(improveTypeId);
            if (improveType == null) {
                Asserts.fail("改善类型不存在,刷新后重新选择");
            }
            improve.setImproveName(improveType.getName());
            improve.setImproveTypeId(improveTypeId);
            improve.setDepartmentType(improveType.getDepartmentType());
            List<ImproveProcess> processList = improve.getProcess();
            processList.remove(processList.size() - 1);
            processList.add(new ImproveProcess(improveType.getUserId(), improveType.getUserName(), ImproveProcessEnum.IN_APPROVAL, null, null, null));
        } else if (Boolean.TRUE.equals(improveDto.getAdopted())){
            Asserts.fail("未选择改善类型");
        }
        improve.setTitle(improveDto.getTitle());
        improve.setRemark(improveDto.getRemark());
        improve.setActionRemark(improveDto.getActionRemark());
        improve.setProceedRemark(improveDto.getProceedRemark());
        improve.setFinish(improveDto.getFinish());
        List<ImproveProcess> process = improve.getProcess();
        ImproveProcess nextProcess = null;
        int precessIndex = 1;
        for (; precessIndex < process.size(); precessIndex++) {
            if (process.get(precessIndex).getOperation() == ImproveProcessEnum.IN_APPROVAL) {
                nextProcess = process.get(precessIndex);
                break;
            }
        }
        if (nextProcess == null) {
            log.info("数据异常,没有下一级审批");
            Asserts.fail("数据异常,没有下一级审批");
        }

        if (Boolean.TRUE.equals(improveDto.getAdopted())) {
            nextProcess.setOperation(ImproveProcessEnum.APPROVED);
            nextProcess.setTime(LocalDateTime.now());
            List<String> followUserIds = improveDto.getFollowUserIds();
            LocalDate followDate = improveDto.getFollowDate();
            improve.setFollowDate(followDate);
            improve.setFollowUserIds(followUserIds);
            if (precessIndex == process.size() - 1) {

                nextProcess.setFollowDate(followDate);
                nextProcess.setFollowUserIds(improveDto.getFollowUserIds());
                if (!CollectionUtils.isEmpty(followUserIds)) {
                    Map<String, String> content = new HashMap<>();
                    if (followDate != null) {
                        content.put("预计完成日期", followDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
                    }
                    this.sendMiniNotice(followUserIds, "有建议需要跟进", "点击查看详情", content);
                }
                improve.setStatus(ImproveStatusEnum.APPROVED);
                this.sendMiniNotice(improve.getUserId(), "建议已通过", "点击查看详情");
            } else {
                ImproveProcess improveProcess = process.get(precessIndex + 1);
                improve.setNextUserName(improveProcess.getUsername());
                improve.setNextUserId(improveProcess.getUserId());
                //通知审批人
                this.sendMiniNotice(improveProcess.getUserId(), "有建议需要审批", "点击查看详情");
            }
        } else {
            this.sendMiniNotice(improve.getUserId(), "建议已拒绝", "点击查看详情");
            nextProcess.setOperation(ImproveProcessEnum.REJECTED);
            nextProcess.setTime(LocalDateTime.now());
            improve.setStatus(ImproveStatusEnum.REJECTED);
            improve.setRejected(improveDto.getRejected());
        }
        this.updateById(improve);
    }

    private void setProcess(Improve improve, ImproveType improveType) throws WxErrorException {
        List<ImproveProcess> processList = new ArrayList<>();
        processList.add(new ImproveProcess(improve.getUserId(), improve.getUserName(), ImproveProcessEnum.SUBMITTED, LocalDateTime.now(), null, null));
        List<WxCpDepart> list = wxCpService.getDepartmentService().list(null);
        Map<Long, WxCpDepart> departMap = list.stream().collect(Collectors.toMap(WxCpDepart::getId, wxCpDepart -> wxCpDepart));
        List<WxCpDepart> departs = new ArrayList<>();
        long departId = improve.getDepartment();
        while (departMap.containsKey(departId)) {
            WxCpDepart wxCpDepart = departMap.get(departId);
            if (!Arrays.asList(wxCpDepart.getDepartmentLeader()).contains(improve.getUserId())) {
                departs.add(wxCpDepart);
            }
            departId = wxCpDepart.getParentId();
        }
        int level = improveType.getLevel().getCode() - (ImproverLevelEnum.DIRECTOR.getCode() - departs.size());
        for (int i = 0; i < level; i++) {
            String departUserId = departs.get(i).getDepartmentLeader()[0];
            WxCpUser cpUser = wxCpService.getUserService().getById(departUserId);
            processList.add(new ImproveProcess(departUserId, cpUser.getName(), ImproveProcessEnum.IN_APPROVAL, null, null, null));
        }
        processList.add(new ImproveProcess(improveType.getUserId(), improveType.getUserName(), ImproveProcessEnum.IN_APPROVAL, null, null, null));
        improve.setProcess(processList);
    }

    private void sendMiniNotice(String toUser, String title, String description) throws WxErrorException {
        this.sendMiniNotice(toUser, title, description, new HashMap<>());
    }

    private void sendMiniNotice(String toUser, String title, String description, Map<String, String> content) throws WxErrorException {
        log.info("sendMiniNotice to user>>{},title>>{},description>>{}", toUser, title, description);
        WxCpMessage wxCpMessage = WxCpMessage.newMiniProgramNoticeBuilder()
                .toUser(toUser)
                .title(title)
                .appId("wx002db6ba4793bb79")
                .description(description)
                .page("pages/index/index")
                .contentItems(content)
                .build();
        wxCpService.getMessageService().send(wxCpMessage);
    }

    private void sendMiniNotice(List<String> toUsers, String title, String description, Map<String, String> content) throws WxErrorException {
        if (CollectionUtils.isEmpty(toUsers)) {
            return;
        }
        for (String toUser : toUsers) {
            this.sendMiniNotice(toUser, title, description, content);
        }
    }

    @Override
    public IPage<Improve> approved(IPage<Improve> page, String userId) {
        return this.baseMapper.approved(page, userId);
    }

    @Override
    public IPage<Improve> follow(IPage<Improve> page, String userId) {
        return this.baseMapper.follow(page, userId);
    }
}




