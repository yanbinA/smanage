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
import com.temple.manage.common.api.CommonPage;
import com.temple.manage.common.api.R;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.common.utils.FileUtil;
import com.temple.manage.domain.ItemResult;
import com.temple.manage.domain.dto.AuditResultDto;
import com.temple.manage.domain.dto.AuditScoreDto;
import com.temple.manage.domain.dto.ExportAuditRecordDto;
import com.temple.manage.domain.vo.AuditRecordVo;
import com.temple.manage.domain.vo.PointAuditRecordVo;
import com.temple.manage.domain.vo.RecordSelectVo;
import com.temple.manage.entity.AuditRecord;
import com.temple.manage.entity.PointAuditRecord;
import com.temple.manage.entity.enums.AuditRecordStatusEnum;
import com.temple.manage.entity.enums.PARStatusEnum;
import com.temple.manage.service.AuditRecordService;
import com.temple.manage.service.MonitoringPointService;
import com.temple.manage.service.PointAuditRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auditRecord")
@Tag(name = "审核记录")
public class AuditRecordController {
    private final AuditRecordService auditRecordService;
    private final PointAuditRecordService pointAuditRecordService;
    private final MonitoringPointService monitoringPointService;

    public AuditRecordController(AuditRecordService auditRecordService,
                                 MonitoringPointService monitoringPointService,
                                 PointAuditRecordService pointAuditRecordService) {
        this.auditRecordService = auditRecordService;
        this.monitoringPointService = monitoringPointService;
        this.pointAuditRecordService = pointAuditRecordService;
    }

    @GetMapping("/list")
    @Operation(summary = "查询审核记录列表", security = @SecurityRequirement(name = "Authorization"),
            parameters = {@Parameter(name = "size", description = "每页数量", example = "10"),
                    @Parameter(name = "current", description = "当前页码", example = "1")})
    public R<CommonPage<AuditRecordVo>> list(@RequestParam(value = "size", defaultValue = "10") Long size,
                                             @RequestParam(value = "current", defaultValue = "1") Long current) {
        Page<AuditRecordVo> page = new Page<>(current, size);
        page.getOrders().add(new OrderItem("modify_time", false));
        IPage<AuditRecordVo> pageInfo = auditRecordService.selectPageVo(page);
        return R.success(CommonPage.restPage(pageInfo));
    }

    @GetMapping("/points")
    @Operation(summary = "查询审核记录的检查点-检查点审核记录列表", security = @SecurityRequirement(name = "Authorization"))
    public R<List<PointAuditRecordVo>> points(@NotNull @RequestParam @Parameter(description = "审核记录id") Integer auditRecordId) {
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getAuditRecordId, auditRecordId)
                .eq(PointAuditRecord::getStatus, PARStatusEnum.SUBMITTED.getCode());
        List<PointAuditRecord> list = this.pointAuditRecordService.list(wrapper);
        return R.success(BeanUtil.copyToList(list, PointAuditRecordVo.class));
    }

    @PostMapping("/updateAudit")
    @Operation(summary = "修改检查项结果", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> updateAudit(@Validated @RequestBody AuditResultDto resultDto) {
        this.monitoringPointService.updateAudit(resultDto);
        return R.success(true);
    }

    @PostMapping("/submitScore")
    @Operation(summary = "修改检查项分数", security = @SecurityRequirement(name = "Authorization"))
    public R<Boolean> submitScore(@Validated @RequestBody AuditScoreDto scoreDto) {
        this.monitoringPointService.updateScore(scoreDto);
        return R.success(true);
    }

    @PostMapping("/exportSelect")
    @Operation(summary = "导出刷选框数据", security = @SecurityRequirement(name = "Authorization"))
    public R<RecordSelectVo> exportSelect(@Validated @RequestBody ExportAuditRecordDto exportSelectDto) {
        LambdaQueryWrapper<AuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(AuditRecord::getCompleteDate, exportSelectDto.getStartTime(), exportSelectDto.getEndTime())
                .eq(AuditRecord::getStatus, AuditRecordStatusEnum.SUBMIT.getCode());
        if (CollectionUtils.isNotEmpty(exportSelectDto.getFactoryAreaIds())) {
            wrapper.in(AuditRecord::getFactoryAreaId, exportSelectDto.getFactoryAreaIds());
        }
        if (CollectionUtils.isNotEmpty(exportSelectDto.getAuditorNames())) {
            wrapper.in(AuditRecord::getAuditorName, exportSelectDto.getAuditorNames());
        }
        List<AuditRecord> list = auditRecordService.list(wrapper);
        Set<RecordSelectVo.FactoryAreaSelect> factoryAreaList = new HashSet<>();
        Set<String> auditorNames = new HashSet<>();
        Set<String> managerNames = new HashSet<>();
        List<Integer> auditRecordIds = new ArrayList<>();
        list.forEach(auditRecord -> {
            factoryAreaList.add(RecordSelectVo.FactoryAreaSelect.builder()
                    .factoryAreaId(auditRecord.getFactoryAreaId())
                    .name(auditRecord.getFactoryAreaName())
                    .build());
            auditorNames.add(auditRecord.getAuditorName());
            auditRecordIds.add(auditRecord.getId());
        });
        if (CollectionUtils.isNotEmpty(auditRecordIds)) {
            LambdaQueryWrapper<PointAuditRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PointAuditRecord::getStatus, PARStatusEnum.SUBMITTED.getCode())
                    .in(PointAuditRecord::getAuditRecordId, auditRecordIds);
            List<PointAuditRecord> auditRecords = pointAuditRecordService.list(queryWrapper);
            managerNames = auditRecords.stream().map(PointAuditRecord::getAuditor).collect(Collectors.toSet());
        }
        RecordSelectVo selectVo = new RecordSelectVo();
        selectVo.setFactoryAreaList(factoryAreaList);
        selectVo.setAuditorNames(auditorNames);
        selectVo.setManagerNames(managerNames);
        return R.success(selectVo);
    }


    @PostMapping("/export")
    @Operation(summary = "导出数据", security = @SecurityRequirement(name = "Authorization"))
    public R<String> export(@Validated @RequestBody ExportAuditRecordDto exportSelectDto) {
        LambdaQueryWrapper<AuditRecord> wrapper = new LambdaQueryWrapper<>();
        LocalDate startTime = exportSelectDto.getStartTime();
        LocalDate endTime = exportSelectDto.getEndTime();
        wrapper.between(AuditRecord::getCompleteDate, startTime, endTime)
                .eq(AuditRecord::getStatus, AuditRecordStatusEnum.SUBMIT.getCode());
        if (CollectionUtils.isNotEmpty(exportSelectDto.getFactoryAreaIds())) {
            wrapper.in(AuditRecord::getFactoryAreaId, exportSelectDto.getFactoryAreaIds());
        }
        if (CollectionUtils.isNotEmpty(exportSelectDto.getAuditorNames())) {
            wrapper.in(AuditRecord::getAuditorName, exportSelectDto.getAuditorNames());
        }
        List<AuditRecord> list = auditRecordService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            Asserts.fail("选择范围内没有可导出数据");
        }
        List<Integer> auditRecordIds = list.stream().map(AuditRecord::getId).collect(Collectors.toList());
        Map<Integer, String> auditRecordIdMap = list.stream().collect(Collectors.toMap(AuditRecord::getId, AuditRecord::getAuditorName));
        LambdaQueryWrapper<PointAuditRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointAuditRecord::getStatus, PARStatusEnum.SUBMITTED.getCode())
                .in(PointAuditRecord::getAuditRecordId, auditRecordIds);
        List<PointAuditRecord> pointAuditRecords = pointAuditRecordService.list(queryWrapper);
        Map<String, List<BigDecimal>> manageScoreMap = new HashMap<>();
        Map<String, AuditImage> auditImageMap = new HashMap<>();
        List<ManageImage> manageImageList = new ArrayList<>();
        List<ScoreDetail> scoreDetailList = new ArrayList<>();
        pointAuditRecords.forEach(pointAuditRecord -> {
            String auditor = pointAuditRecord.getAuditor();
            List<BigDecimal> scoreList = manageScoreMap.getOrDefault(auditor, new ArrayList<>());
            scoreList.add(pointAuditRecord.getTotalScore());
            manageScoreMap.putIfAbsent(auditor, scoreList);
            //Set<String> unqualifiedUrlList = pointAuditRecord.getUnqualifiedUrlList();
            List<ItemResult> unqualifiedUrlList = pointAuditRecord.getItemList().stream().filter(item -> !item.getQualified()).collect(Collectors.toList());
            String date = pointAuditRecord.getModifyTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
            if (CollectionUtils.isNotEmpty(unqualifiedUrlList)) {
                unqualifiedUrlList.forEach(item -> {
                    ManageImage manageImage = new ManageImage();
                    manageImage.setId(1);
                    manageImage.setDate(date);
                    manageImage.setAreaName(pointAuditRecord.getAreaName().concat("-").concat(pointAuditRecord.getSerialNumber()).concat("-").concat(item.getItemNumber()));
                    manageImage.setName(auditor);
                    manageImage.setUrl(item.getImage());
                    manageImageList.add(manageImage);
                });
            }
            String name = auditRecordIdMap.get(pointAuditRecord.getAuditRecordId());
            AuditImage auditImage = auditImageMap.getOrDefault(name, new AuditImage());
            auditImage.setName(name);
            int total = auditImage.getTotal() + Optional.of(unqualifiedUrlList).map(List::size).orElse(0);
            auditImage.setTotal(total);
            auditImageMap.put(name, auditImage);
            ScoreDetail scoreDetail = new ScoreDetail();
            scoreDetail.setDate(date);
            scoreDetail.setName(name);
            scoreDetail.setAreaName(pointAuditRecord.getAreaName().concat("-").concat(pointAuditRecord.getSerialNumber()));
            scoreDetail.setAudit(auditor);
            scoreDetail.setPositionScore(pointAuditRecord.getPositionScore());
            scoreDetail.setPositionImage(pointAuditRecord.getPositionImage());
            scoreDetail.setPositionRemark(pointAuditRecord.getPositionRemark());
            scoreDetail.setCleanScore(pointAuditRecord.getCleanScore());
            scoreDetail.setCleanImage(pointAuditRecord.getCleanImage());
            scoreDetail.setCleanRemark(pointAuditRecord.getCleanRemark());

            scoreDetailList.add(scoreDetail);

        });
        List<ManageScore> manageScoreList = new ArrayList<>();
        manageScoreMap.forEach((name, scoreList) -> {
            ManageScore manageScore = new ManageScore();
            BigDecimal avg = BigDecimal.ZERO;
            if (scoreList.size() != 0) {
                BigDecimal sum = BigDecimal.ZERO;
                for (BigDecimal item : scoreList) {
                    sum = sum.add(item);
                }
                avg = sum.divide(BigDecimal.valueOf(scoreList.size()), 2, RoundingMode.UP);
            }
            manageScore.setScore(avg);
            manageScore.setName(name);
            manageScoreList.add(manageScore);
        });
        manageScoreList.sort((score1, score2) -> -score1.getScore().compareTo(score2.getScore()));
        List<AuditImage> auditImageList = new ArrayList<>(auditImageMap.values());
        auditImageList.sort((image1, image2) -> image2.getTotal() - image1.getTotal());
        String fileName = "s5_" + System.currentTimeMillis() + ".xls";
        fileName = FileUtil.generalDir().concat(fileName);
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
        ExcelWriter excelWriter = null;

        excelWriter = EasyExcel.write(fileName).withTemplate(this.getClass().getClassLoader().getResourceAsStream("excel/s5.xls")).build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet().build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(1).build();
        for (int i = 0; i < manageScoreList.size(); i++) {
            manageScoreList.get(i).setId(i + 1);
            manageScoreList.get(i).setSort(i + 1);
        }
        for (int i = 0; i < auditImageList.size(); i++) {
            auditImageList.get(i).setId(i + 1);
            auditImageList.get(i).setSort(i + 1);
        }
        for (int i = 0; i < manageImageList.size(); i++) {
            manageImageList.get(i).setId(i + 1);
        }
        excelWriter.fill(new FillWrapper("score", manageScoreList), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("image", manageImageList), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("audit", auditImageList), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("item", scoreDetailList), fillConfig, writeSheet2);
        Map<String, String> map = new HashMap<>();
        map.put("dateSpan", startTime.format(DateTimeFormatter.ISO_LOCAL_DATE).concat("到").concat(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        excelWriter.fill(map, writeSheet1);
        excelWriter.finish();
        return R.success(FileUtil.getUrl(fileName));
    }

    @Data
    static class ManageScore{
        private Integer id;
        private Integer sort;
        private BigDecimal score;
        private String name;
    }

    @Data
    static class ManageImage{
        private Integer id;
        private String date;
        private String areaName;
        private String name;
        private String url = "";
    }

    @Data
    static class AuditImage{
        private Integer id;
        private Integer sort;
        private int total;
        private String name;
    }

    @Data
    static class ScoreDetail {
        private String date;
        private String name;
        private String areaName;
        private String audit;
        private Integer positionScore;
        private String positionImage;
        private String positionRemark;
        private Integer cleanScore;
        private String cleanImage;
        private String cleanRemark;
    }

}