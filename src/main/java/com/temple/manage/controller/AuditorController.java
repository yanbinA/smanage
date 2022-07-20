package com.temple.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temple.manage.common.api.R;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.domain.dto.AuditResultDto;
import com.temple.manage.domain.dto.AuditScoreDto;
import com.temple.manage.domain.dto.FactoryAuditDto;
import com.temple.manage.domain.vo.FactoryAreaVo;
import com.temple.manage.domain.vo.MonitoringItemVo;
import com.temple.manage.domain.vo.MonitoringPointAuditVo;
import com.temple.manage.entity.FactoryArea;
import com.temple.manage.entity.MonitoringItem;
import com.temple.manage.entity.MonitoringPoint;
import com.temple.manage.entity.PointAuditRecord;
import com.temple.manage.entity.enums.PARStatusEnum;
import com.temple.manage.security.SecurityUtils;
import com.temple.manage.service.FactoryAreaService;
import com.temple.manage.service.MonitoringPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auditor")
@Tag(name = "审核员")
@Validated
public class AuditorController {
    private final FactoryAreaService factoryAreaService;
    private final MonitoringPointService monitoringPointService;

    public AuditorController(FactoryAreaService factoryAreaService, MonitoringPointService monitoringPointService) {
        this.factoryAreaService = factoryAreaService;
        this.monitoringPointService = monitoringPointService;
    }

    @GetMapping("/areas")
    @Operation(summary = "查询所有车间", security = @SecurityRequirement(name = "Authorization"))
    public R<List<FactoryAreaVo>> areas() {
        LambdaQueryWrapper<FactoryArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FactoryArea::getIsDeleted, false);
        List<FactoryArea> areas = this.factoryAreaService.list(wrapper);
        return R.success(areas.stream().map(area -> {
            FactoryAreaVo factoryAreaVo = new FactoryAreaVo();
            BeanUtils.copyProperties(area, factoryAreaVo);
            LambdaQueryWrapper<MonitoringPoint> pointWrapper = new LambdaQueryWrapper<>();
            pointWrapper.eq(MonitoringPoint::getFactoryAreaId, area.getId())
                    .eq(MonitoringPoint::getIsDeleted, false);
            int count = monitoringPointService.list(pointWrapper).stream().mapToInt(MonitoringPoint::getItemCount).sum();
            factoryAreaVo.setCount(count);
            return factoryAreaVo;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/points")
    @Operation(summary = "查询所有检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<List<MonitoringPointAuditVo>> points(@NotNull @RequestParam @Parameter(description = "车间id") Integer factoryAreaId) {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        if (username.isEmpty()) {
            Asserts.fail(ResultCode.USER_NOT_EXIST);
        }
        return R.success(monitoringPointService.listByAuditor(factoryAreaId, username.get()));
    }

    @GetMapping("/offline/points")
    @Operation(summary = "离线查询所有检查点", security = @SecurityRequirement(name = "Authorization"))
    public R<List<MonitoringPointAuditVo>> offlinePoints(@NotNull @RequestParam @Parameter(description = "车间id") Integer factoryAreaId) {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        if (username.isEmpty()) {
            Asserts.fail(ResultCode.USER_NOT_EXIST);
        }
        List<MonitoringPointAuditVo> list = monitoringPointService.listByAuditor(factoryAreaId, username.get());
        list.forEach(detailVo -> {
            int pointId = detailVo.getId();
            PARStatusEnum pointRecordStatus = detailVo.getStatus();
            detailVo.setStatus(pointRecordStatus);
            List<MonitoringItem> itemList = this.monitoringPointService.listMonitoringItemByPointId(pointId);
            detailVo.setItemList(BeanUtil.copyToList(itemList, MonitoringItemVo.class));
        });
        return R.success(list);
    }

    @GetMapping("/getPointById")
    @Operation(summary = "查询检查点详情", security = @SecurityRequirement(name = "Authorization"))
    public R<MonitoringPointAuditVo> getById(@NotNull @RequestParam @Parameter(description = "检查点id") Integer pointId,
                                             @NotNull @RequestParam @Parameter(description = "检查记录id") Integer auditRecordId) {
        MonitoringPoint monitoringPoint = this.monitoringPointService.getById(pointId);
        MonitoringPointAuditVo detailVo = new MonitoringPointAuditVo();
        BeanUtils.copyProperties(monitoringPoint, detailVo);
        PointAuditRecord pointAuditRecord = this.monitoringPointService.getPointRecordStatus(auditRecordId, pointId);
        PARStatusEnum pointRecordStatus = Optional.ofNullable(pointAuditRecord)
                .map(PointAuditRecord::getStatus)
                .orElse(PARStatusEnum.SUBMIT_NOT_EXIST);
        detailVo.setStatus(pointRecordStatus);
        detailVo.setAuditRecordId(auditRecordId);
        if (pointRecordStatus == PARStatusEnum.SUBMIT_NOT_EXIST) {
            List<MonitoringItem> itemList = this.monitoringPointService.listMonitoringItemByPointId(pointId);
            detailVo.setItemList(BeanUtil.copyToList(itemList, MonitoringItemVo.class));
        } else {
            detailVo.setItemList(BeanUtil.copyToList(pointAuditRecord.getItemList(), MonitoringItemVo.class));
            detailVo.setCleanImage(pointAuditRecord.getCleanImage());
            detailVo.setCleanRemark(pointAuditRecord.getCleanRemark());
            detailVo.setCleanScore(pointAuditRecord.getCleanScore());
            detailVo.setPositionImage(pointAuditRecord.getPositionImage());
            detailVo.setPositionScore(pointAuditRecord.getPositionScore());
            detailVo.setPositionRemark(pointAuditRecord.getPositionRemark());
        }
        return R.success(detailVo);
    }


    @PostMapping("/submitAudit")
    @Operation(summary = "提交检查项结果", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> submitAudit(@Validated @RequestBody AuditResultDto resultDto) {
        this.monitoringPointService.submitAudit(resultDto);
        return R.success(true);
    }

    @PostMapping("/submitScore")
    @Operation(summary = "提交检查项分数", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> submitScore(@Validated @RequestBody AuditScoreDto scoreDto) {
        this.monitoringPointService.submitScore(scoreDto);
        return R.success(true);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交车间数据", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> submit(@NotNull @RequestParam @Parameter(description = "车间id") Integer factoryAreaId) {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        if (username.isEmpty()) {
            Asserts.fail(ResultCode.USER_NOT_EXIST);
        }
        this.monitoringPointService.submit(factoryAreaId, username.get());
        return R.success(true);
    }

    @PostMapping("offline/submit")
    @Operation(summary = "离线提交车间检查结果", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> offlineSubmit(@Validated @RequestBody FactoryAuditDto factoryAuditDto) {
        Optional<String> username = SecurityUtils.getCurrentUsername();
        if (username.isEmpty()) {
            Asserts.fail(ResultCode.USER_NOT_EXIST);
        }
        String user = username.get();
        Integer factoryAreaId = factoryAuditDto.getFactoryAreaId();
        Integer auditRecordId = factoryAuditDto.getAuditRecordId();
        factoryAuditDto.getList().forEach(auditResult -> {
            AuditResultDto resultDto = new AuditResultDto();
            resultDto.setPointId(auditResult.getPointId());
            resultDto.setFactoryAreaId(factoryAreaId);
            resultDto.setAuditRecordId(auditRecordId);
            resultDto.setItemList(auditResult.getItemList());
            this.monitoringPointService.submitAudit(resultDto);
            AuditScoreDto auditScoreDto = new AuditScoreDto();
            auditScoreDto.setPointId(auditResult.getPointId());
            auditScoreDto.setFactoryAreaId(factoryAreaId);
            auditScoreDto.setAuditRecordId(auditRecordId);
            auditScoreDto.setPositionScore(auditResult.getPositionScore());
            auditScoreDto.setPositionRemark(auditResult.getPositionRemark());
            auditScoreDto.setPositionImage(auditResult.getPositionImage());
            auditScoreDto.setCleanScore(auditResult.getCleanScore());
            auditScoreDto.setCleanRemark(auditResult.getCleanRemark());
            auditScoreDto.setCleanImage(auditResult.getCleanImage());
            this.monitoringPointService.submitScore(auditScoreDto);
        });
        this.monitoringPointService.submit(factoryAreaId, username.get());
        return R.success(true);
    }


}
