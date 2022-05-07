package com.temple.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.temple.manage.common.api.CommonPage;
import com.temple.manage.common.api.R;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.common.utils.FileUtil;
import com.temple.manage.common.validators.group.Modify;
import com.temple.manage.domain.dto.ExportImproveDto;
import com.temple.manage.domain.dto.ImproveDto;
import com.temple.manage.domain.dto.ImproveTypeDto;
import com.temple.manage.domain.vo.ImproveTypeVo;
import com.temple.manage.entity.Improve;
import com.temple.manage.entity.ImproveType;
import com.temple.manage.entity.Role;
import com.temple.manage.entity.enums.ImproveDepartmentEnum;
import com.temple.manage.entity.enums.ImproveStatusEnum;
import com.temple.manage.security.SecurityUtils;
import com.temple.manage.security.jwt.TokenProvider;
import com.temple.manage.service.ImproveService;
import com.temple.manage.service.ImproveTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpMaJsCode2SessionResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 建议审批
 * </p>
 *
 * @author messi
 * @package com.temple.manage.controller
 * @description 建议审批
 * @date 2022-01-07 0:19
 * @verison V1.0.0
 */
@RestController
@RequestMapping("/api/improve")
@Slf4j
@Tag(name = "建议审批")
@Validated
@RequiredArgsConstructor
public class ImproveController {
    private final ImproveService improveService;
    private final ImproveTypeService improveTypeService;
    private final WxCpService wxCpService;
    private final TokenProvider tokenProvider;



    @GetMapping("/authenticate")
    @Operation(summary = "小程序用户登录")
    public R<JWTSession> authorizeWx(@NotBlank @RequestParam() String code) throws WxErrorException {
        WxCpMaJsCode2SessionResult sessionResult = wxCpService.jsCode2Session(code);
        String sessionKey = sessionResult.getSessionKey();
        String userId = sessionResult.getUserId();
        //WxCpMaJsCode2SessionResult sessionResult = new WxCpMaJsCode2SessionResult();
        //String sessionKey = "sessionResult.getSessionKey()";
        //String userId = code;
        //sessionResult.setUserId(code);

        Role role = new Role();
        role.setName("ROLE_AUDIT");
        Authentication authentication = new RememberMeAuthenticationToken(sessionKey, userId, Collections.singleton(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JWTSession jwtSession = new JWTSession();
        BeanUtils.copyProperties(sessionResult, jwtSession);
        String jwt = tokenProvider.createToken(authentication, true);
        jwtSession.setIdToken(jwt);
        return R.success(jwtSession);
    }

    @Data
    @Schema(description = "session信息")
    static class JWTSession {
        @JsonProperty("token")
        @Schema(description = "token")
        private String idToken;
        @JsonProperty("session_key")
        private String sessionKey;

        @JsonProperty("userid")
        private String userId;

        @JsonProperty("corpid")
        private String corpId;
    }

    @PostMapping("/addType")
    @Operation(summary = "添加类型", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> addType(@RequestBody @Validated ImproveTypeDto improveTypeDto) throws WxErrorException {
        ImproveType improveType = new ImproveType();
        BeanUtils.copyProperties(improveTypeDto, improveType);
        WxCpUser cpUser = wxCpService.getUserService().getById(improveType.getUserId());
        improveType.setUserName(cpUser.getName());
        return R.success(improveTypeService.save(improveType));
    }

    @PostMapping("/modifyType")
    @Operation(summary = "修改类型", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> modifyType(@RequestBody @Validated(Modify.class) ImproveTypeDto improveTypeDto) throws WxErrorException {
        ImproveType improveType = improveTypeService.getById(improveTypeDto.getId());
        improveType.setName(improveTypeDto.getName());
        improveType.setUserId(improveTypeDto.getUserId());
        improveType.setDepartmentType(improveTypeDto.getDepartmentType());
        improveType.setLevel(improveTypeDto.getLevel());
        WxCpUser cpUser = wxCpService.getUserService().getById(improveType.getUserId());
        improveType.setUserName(cpUser.getName());
        return R.success(improveTypeService.updateById(improveType));
    }

    @GetMapping("/deleteType")
    @Operation(summary = "删除类型", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> deleteType(@NotNull Integer id){
        return R.success(improveTypeService.removeById(id));
    }

    @GetMapping("/listType")
    @Operation(summary = "类型列表", security = @SecurityRequirement(name = "Authorization"))
    public R<List<ImproveTypeVo>> listType(){
        List<ImproveType> list = improveTypeService.list();
        return R.success(BeanUtil.copyToList(list, ImproveTypeVo.class));
    }

    @GetMapping("/listSubmitted")
    @Operation(summary = "查询自己提交的建议列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量"),
                    @Parameter(name = "current", description = "当前页码")})
    public R<CommonPage<Improve>> listSubmitted(@RequestParam(value = "size", defaultValue = "10") Long size,
                                    @RequestParam(value = "current", defaultValue = "1") Long current) {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        LambdaQueryWrapper<Improve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Improve::getUserId, userId);
        Page<Improve> page = new Page<>(current, size);
        page.getOrders().add(new OrderItem("create_time", false));
        Page<Improve> pageInfo = improveService.page(page, wrapper);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @GetMapping("/listApproval")
    @Operation(summary = "查询待审批的建议列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量"),
                    @Parameter(name = "current", description = "当前页码")})
    public R<CommonPage<Improve>> listApproval(@RequestParam(value = "size", defaultValue = "10") Long size,
                                                @RequestParam(value = "current", defaultValue = "1") Long current) {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        LambdaQueryWrapper<Improve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Improve::getNextUserId, userId);
        wrapper.eq(Improve::getStatus, ImproveStatusEnum.IN_APPROVAL.getCode());
        wrapper.orderByDesc(Improve::getModifyTime);
        Page<Improve> page = new Page<>(current, size);
        Page<Improve> pageInfo = improveService.page(page, wrapper);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交合理建议", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> submit(@Validated @RequestBody ImproveDto improveDto) throws WxErrorException {
        Integer improveTypeId = improveDto.getImproveTypeId();
        if (Boolean.TRUE.equals(improveDto.getFinish())) {
            if (improveTypeId == null) {
                Asserts.fail(ResultCode.VALIDATE_FAILED);
            }
            //if (StringUtils.isEmpty(improveDto.getActionRemark())) {
            //    Asserts.fail(ResultCode.VALIDATE_FAILED);
            //}
            //if (StringUtils.isEmpty(improveDto.getProceedRemark())) {
            //    Asserts.fail(ResultCode.VALIDATE_FAILED);
            //}
        }
        improveService.submit(improveDto);
        return R.success(true);
    }

    @PostMapping("/adopt")
    @Operation(summary = "审批建议", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> adopt(@Validated(Modify.class) @RequestBody ImproveDto improveDto) throws WxErrorException {

        Improve improve = new Improve();
        BeanUtils.copyProperties(improveDto, improve);
        improveService.adopt(improveDto);
        return R.success(true);
    }

    @GetMapping("/user/list")
    @Operation(summary = "获取部门成员详情", security = @SecurityRequirement(name = "Authorization"))
    public R<List<WxCpUser>> userList(@NotNull Long departmentId) throws WxErrorException {
        return R.success(wxCpService.getUserService().listByDepartment(departmentId, false, null));
    }

    @GetMapping("/list")
    @Operation(summary = "查询全部已办建议列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量"),
                    @Parameter(name = "current", description = "当前页码")})
    public R<CommonPage<Improve>> list(@RequestParam(value = "size", defaultValue = "10") Long size,
                                               @RequestParam(value = "current", defaultValue = "1") Long current) {
        LambdaQueryWrapper<Improve> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Improve::getStatus, ImproveStatusEnum.IN_APPROVAL.getCode());
        wrapper.orderByDesc(Improve::getModifyTime);
        Page<Improve> page = new Page<>(current, size);
        Page<Improve> pageInfo = improveService.page(page, wrapper);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @GetMapping("/approvalList")
    @Operation(summary = "查询自己已办建议列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量"),
                    @Parameter(name = "current", description = "当前页码")})
    public R<CommonPage<Improve>> approvalList(@RequestParam(value = "size", defaultValue = "10") Long size,
                                       @RequestParam(value = "current", defaultValue = "1") Long current) {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        Page<Improve> page = new Page<>(current, size);
        page.getOrders().add(new OrderItem("modify_time", false));
        IPage<Improve> pageInfo = improveService.approved(page, userId);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @GetMapping("/followList")
    @Operation(summary = "查询自己跟进建议列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量"),
                    @Parameter(name = "current", description = "当前页码")})
    public R<CommonPage<Improve>> followList(@RequestParam(value = "size", defaultValue = "10") Long size,
                                               @RequestParam(value = "current", defaultValue = "1") Long current) {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        Page<Improve> page = new Page<>(current, size);
        page.getOrders().add(new OrderItem("modify_time", false));
        IPage<Improve> pageInfo = improveService.follow(page, userId);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @PostMapping("/export")
    @Operation(summary = "导出数据", security = @SecurityRequirement(name = "Authorization"))
    public R<String> export(@Validated @RequestBody ExportImproveDto exportSelectDto) throws IOException {
        String userId = SecurityUtils.getCurrentUsername().orElseThrow(() -> Asserts.throwException(ResultCode.USER_NOT_EXIST));
        LambdaQueryWrapper<Improve> wrapper = new LambdaQueryWrapper<>();
        LocalDate startTime = exportSelectDto.getStartTime();
        LocalDate endTime = exportSelectDto.getEndTime();
        wrapper.between(Improve::getCreateTime, startTime, endTime)
                .ne(Improve::getStatus, ImproveStatusEnum.IN_APPROVAL.getCode());
        if (exportSelectDto.getFinish() != null) {
            wrapper.eq(Improve::getFinish, exportSelectDto.getFinish());
        }
        if (exportSelectDto.getDepartmentType() != null) {
            wrapper.in(Improve::getDepartmentType, exportSelectDto.getDepartmentType());
        }
        if (exportSelectDto.getImproveTypeId() != null) {
            wrapper.in(Improve::getImproveTypeId, exportSelectDto.getImproveTypeId());
        }
        List<Improve> list = improveService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            Asserts.fail("选择范围内没有可导出数据");
        }
        Map<Long, String> departmentMap = new HashMap<>();
        List<ImproveItem> improveItemList = IntStream.range(0, list.size())
                .mapToObj(i -> {
                    Improve item = list.get(i);
                    return getImproveItem(departmentMap, i, item);
                }).collect(Collectors.toList());

        Map<String, List<ImproveItem>> userMap = improveItemList.stream()
                .collect(Collectors.groupingBy(ImproveItem::getUsername));
        List<UserImprove> userImproves = new ArrayList<>();
        userMap.forEach((username, itemList) -> {
            UserImprove userImprove = new UserImprove();
            userImprove.setDepartment(itemList.get(0).department);
            userImprove.setUsername(username);
            userImprove.setTimes(itemList.size());
            userImprove.setImproveType(itemList.stream()
                    .map(ImproveItem::getImproveType)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .collect(Collectors.joining(",")));
            userImproves.add(userImprove);
        });
        userImproves.sort((user1, user2) -> user2.getTimes() - user1.getTimes());
        for (int i = 0; i < userImproves.size(); i++) {
            UserImprove userImprove = userImproves.get(i);
            userImprove.setId(i + 1);
            userImprove.setSort(i + 1);
        }
        Map<String, List<ImproveItem>> departments = improveItemList.stream().collect(Collectors.groupingBy(ImproveItem::getDepartment));
        List<DepartmentImprove> departmentImproves = new ArrayList<>();
        departments.forEach((department, itemList) -> {
            DepartmentImprove departmentImprove = new DepartmentImprove();
            departmentImprove.setId(departmentImproves.size() + 1);
            departmentImprove.setDepartment(department);
            departmentImprove.setTotal(itemList.size());
            List<WxCpUser> wxCpUsers = null;
            try {
                wxCpUsers = wxCpService.getUserService().listByDepartment(itemList.get(0).departmentId, false, null);
            } catch (WxErrorException e) {
                log.error("获取企业部门数据为NULL:{}", department);
            }
            if (CollectionUtils.isEmpty(wxCpUsers)) {
                departmentImprove.setCount(1);
            } else {
                departmentImprove.setCount(wxCpUsers.size());
            }
            departmentImprove.setAvg(BigDecimal.valueOf(departmentImprove.getTotal())
                    .divide(BigDecimal.valueOf(departmentImprove.getCount()), 4, RoundingMode.UP));
            departmentImprove.setApproved((int) itemList.stream().filter(ImproveItem::isApproved).count());
            departmentImprove.setAvgApproved(BigDecimal.valueOf(departmentImprove.getApproved())
                    .divide(BigDecimal.valueOf(departmentImprove.getCount()), 4, RoundingMode.UP));
            departmentImprove.setApprovedRate(BigDecimal.valueOf(departmentImprove.getApproved())
                    .divide(BigDecimal.valueOf(departmentImprove.getTotal()), 4, RoundingMode.UP)
                    .divide(BigDecimal.valueOf(departmentImprove.getCount()), 4, RoundingMode.UP));
            departmentImproves.add(departmentImprove);
        });

        String fileName = "improve_" + System.currentTimeMillis() + ".xls";
        fileName = FileUtil.generalDir().concat(fileName);
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
        ExcelWriter excelWriter = null;
        List<ImproveItem> improveItems = improveItemList.stream()
                .filter(item -> item.approved && item.departmentType == ImproveDepartmentEnum.QUALITY)
                .collect(Collectors.toList());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("excel/improve.xls")));
        if (improveItems.size() > 0) {
            workbook.setSheetName(1, WorkbookUtil.createSafeSheetName("1.".concat(improveItems.get(0).getTitle())));
            for (int i = 1; i < improveItems.size(); i++) {
                workbook.cloneSheet(1);
                workbook.setSheetName(i + 1, WorkbookUtil.createSafeSheetName(String.valueOf(i + 1).concat(".").concat(improveItems.get(i).getTitle())));
            }
        } else {
            workbook.removeSheetAt(1);
        }
        workbook.write(bos);
        excelWriter = EasyExcel.write(fileName).withTemplate(new ByteArrayInputStream(bos.toByteArray())).build();

        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet(0).build();
        excelWriter.fill(new FillWrapper("list", improveItemList), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("user", userImproves), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("department", departmentImproves), fillConfig, writeSheet1);
        for (int i = 0; i < improveItems.size(); i++) {
            excelWriter.fill(improveItems.get(i), EasyExcel.writerSheet(i + 1).build());
        }
        excelWriter.finish();
        try {
            WxMediaUploadResult upload = wxCpService.getMediaService().upload(WxConsts.MediaFileType.FILE, new File(fileName));
            WxCpMessage wxCpMessage = WxCpMessage.FILE()
                    .toUser(userId)
                    .mediaId(upload.getMediaId())
                    .build();
            wxCpService.getMessageService().send(wxCpMessage);
        } catch (WxErrorException e) {
            log.error("upload and send error", e);
        }
        return R.success(FileUtil.getUrl(fileName));
    }

    private ImproveItem getImproveItem(Map<Long, String> departmentMap, int i, Improve item) {
        ImproveItem improveItem = new ImproveItem();
        improveItem.setId(i + 1);
        long department = item.getDepartment();
        if (departmentMap.containsKey(department)) {
            improveItem.setDepartment(departmentMap.get(department));
        } else {
            List<WxCpDepart> wxCpDeparts = null;
            try {
                wxCpDeparts = Optional.ofNullable(wxCpService.getDepartmentService().list(department))
                        .orElseThrow(() -> Asserts.throwException("获取企业部门数据为NULL:" + department));
            } catch (WxErrorException e) {
                log.error("获取企业部门数据为NULL:{}", department);
            }
            if (wxCpDeparts == null) {
                Asserts.fail("获取企业部门数据为NULL");
            }
            WxCpDepart depart = null;
            for (WxCpDepart wxCpDepart : wxCpDeparts) {
                if (wxCpDepart.getId() == department) {
                    depart = wxCpDepart;
                    break;
                }
            }
            if (depart == null) {
                Asserts.fail("获取企业部门数据为NULL");
            }
            improveItem.setDepartment(depart.getName());
            departmentMap.put(department, depart.getName());
        }
        improveItem.setDepartmentType(item.getDepartmentType());
        improveItem.setRemark(item.getRemark());
        improveItem.setActionRemark(item.getActionRemark());
        improveItem.setCreateTime(item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        improveItem.setMonth(item.getCreateTime().getMonthValue() + "月");
        improveItem.setUsername(item.getUserName());
        improveItem.setTitle(item.getTitle());
        improveItem.setDepartmentId(item.getDepartment());
        improveItem.setApproved(item.getStatus() == ImproveStatusEnum.APPROVED);
        improveItem.setFinish(Boolean.TRUE.equals(item.getFinish()) ? "已完成" : "未完成");
        improveItem.setImproveType(Optional.ofNullable(improveTypeService.getById(item.getImproveTypeId()))
                .map(ImproveType::getName).orElse(""));
        improveItem.setType(Optional.ofNullable(item.getDepartmentType()).map(ImproveDepartmentEnum::getName).orElse(""));
        return improveItem;
    }

    //内容清单
    @Data
    static class ImproveItem{
        private Integer id;
        private String department;
        private Long departmentId;
        private String month;
        private String username;
        private String title;
        private String remark;
        private String actionRemark;
        private String createTime;
        private String finish;
        private String improveType;
        private String type;
        private String money;
        private boolean approved;
        private ImproveDepartmentEnum departmentType;
    }

    @Data
    static class UserImprove{
        private Integer id;
        private String department;
        private String username;
        private Integer times;
        private String improveType;
        private Integer sort;
    }

    @Data
    static class DepartmentImprove{
        private Integer id;
        private String department;
        private Integer total;
        private Integer count;
        private BigDecimal avg;
        private BigDecimal avgApproved;
        private BigDecimal approvedRate;
        private Integer approved;
    }

}
