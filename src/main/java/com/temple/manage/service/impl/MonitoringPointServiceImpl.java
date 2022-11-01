package com.temple.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.domain.ItemResult;
import com.temple.manage.domain.dto.AuditResultDto;
import com.temple.manage.domain.dto.AuditScoreDto;
import com.temple.manage.domain.dto.MonitoringPointDto;
import com.temple.manage.domain.vo.MonitoringPointAuditVo;
import com.temple.manage.entity.*;
import com.temple.manage.entity.enums.AuditRecordStatusEnum;
import com.temple.manage.entity.enums.PARStatusEnum;
import com.temple.manage.mapper.MonitoringPointMapper;
import com.temple.manage.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author messi
* @description 针对表【s_monitoring_point(监测点)】的数据库操作Service实现
* @createDate 2021-12-27 20:46:45
*/
@Slf4j
@Service
public class MonitoringPointServiceImpl extends ServiceImpl<MonitoringPointMapper, MonitoringPoint>
    implements MonitoringPointService{
    private final MonitoringItemService monitoringItemService;
    private final AuditRecordService auditRecordService;
    private final AuditResultService auditResultService;
    private final PointAuditRecordService pointAuditRecordService;


    public MonitoringPointServiceImpl(MonitoringItemService monitoringItemService,
                                      AuditRecordService auditRecordService,
                                      AuditResultService auditResultService,
                                      PointAuditRecordService pointAuditRecordService) {
        this.monitoringItemService = monitoringItemService;
        this.auditRecordService = auditRecordService;
        this.auditResultService = auditResultService;
        this.pointAuditRecordService = pointAuditRecordService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean insert(MonitoringPointDto monitoringPointDto) {
        MonitoringPoint monitoringPoint = new MonitoringPoint();
        BeanUtils.copyProperties(monitoringPointDto, monitoringPoint);
        monitoringPoint.setSerialNumber(String.valueOf(monitoringPointDto.getSerialNumber()));
        this.changeSerialNumber(monitoringPoint);
        this.save(monitoringPoint);
        final Integer monitoringPointId = monitoringPoint.getId();
        List<MonitoringItem> itemList = monitoringPointDto.getItemList().stream().map(item -> {
            MonitoringItem monitoringItem = new MonitoringItem();
            BeanUtils.copyProperties(item, monitoringItem);
            monitoringItem.setMonitoringPointId(monitoringPointId);
            return monitoringItem;
        }).collect(Collectors.toList());
        this.monitoringItemService.saveBatch(itemList);
        monitoringPoint.setItemCount(itemList.size());
        return this.updateById(monitoringPoint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean modify(MonitoringPointDto monitoringPointDto) {
        if (this.getById(monitoringPointDto.getId()) == null) {
            Asserts.fail(ResultCode.NO_DATA);
        }
        MonitoringPoint monitoringPoint = new MonitoringPoint();
        BeanUtils.copyProperties(monitoringPointDto, monitoringPoint);
        monitoringPoint.setSerialNumber(String.valueOf(monitoringPointDto.getSerialNumber()));
        this.changeSerialNumber(monitoringPoint);
        this.baseMapper.updateById(monitoringPoint);
        final Integer monitoringPointId = monitoringPoint.getId();
        LambdaQueryWrapper<MonitoringItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringItem::getMonitoringPointId, monitoringPointId)
                .eq(MonitoringItem::getIsDeleted, false);
        List<MonitoringItem> monitoringItems = this.monitoringItemService.list(wrapper);
        Set<Integer> itemIds = monitoringItems.stream().map(MonitoringItem::getId).collect(Collectors.toSet());
        monitoringPointDto.getItemList().forEach(item -> {
            Integer itemId = item.getId();
            if (itemId != null) {
                itemIds.remove(itemId);
            }
            MonitoringItem monitoringItem = new MonitoringItem();
            BeanUtils.copyProperties(item, monitoringItem);
            monitoringItem.setMonitoringPointId(monitoringPointId);
            this.monitoringItemService.saveOrUpdate(monitoringItem);
        });
        this.monitoringItemService.removeByIds(itemIds);
        monitoringPoint.setItemCount(monitoringPointDto.getItemList().size());
        return this.updateById(monitoringPoint);
    }

    @Override
    public boolean deleteById(Integer id) {
        MonitoringPoint monitoringPoint = this.getById(id);
        LambdaQueryWrapper<MonitoringPoint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitoringPoint::getFactoryAreaId, monitoringPoint.getFactoryAreaId())
                .eq(MonitoringPoint::getAreaName, monitoringPoint.getAreaName())
                .ge(MonitoringPoint::getSerialNumber, Long.parseLong(monitoringPoint.getSerialNumber()));
        List<MonitoringPoint> list = this.list(queryWrapper);
        log.info("delete changeSerialNumber,list,{}", list);
        list.forEach(point -> point.setSerialNumber(String.valueOf(Long.parseLong(point.getSerialNumber()) - 1)));
        this.updateBatchById(list);
        return this.removeById(id);
    }

    private void changeSerialNumber(MonitoringPoint monitoringPoint) {
        log.info("changeSerialNumber,{}", monitoringPoint);
        LambdaQueryWrapper<MonitoringPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPoint::getFactoryAreaId, monitoringPoint.getFactoryAreaId())
                .eq(MonitoringPoint::getAreaName, monitoringPoint.getAreaName())
                .eq(MonitoringPoint::getSerialNumber, monitoringPoint.getSerialNumber());
        int count = this.count(wrapper);
        if (count > 0) {
            LambdaQueryWrapper<MonitoringPoint> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MonitoringPoint::getFactoryAreaId, monitoringPoint.getFactoryAreaId())
                    .eq(MonitoringPoint::getAreaName, monitoringPoint.getAreaName())
                    .ge(MonitoringPoint::getSerialNumber, Long.parseLong(monitoringPoint.getSerialNumber()));
            List<MonitoringPoint> list = this.list(queryWrapper);
            log.info("changeSerialNumber,list,{}", list);
            list.forEach(point -> point.setSerialNumber(String.valueOf(Long.parseLong(point.getSerialNumber()) + 1)));
            this.updateBatchById(list);
        }
    }

    @Override
    public List<MonitoringPointAuditVo> listByAuditor(Integer factoryAreaId, String auditorName) {
        LambdaQueryWrapper<MonitoringPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPoint::getFactoryAreaId, factoryAreaId)
                .eq(MonitoringPoint::getIsDeleted, false)
                .orderByAsc(MonitoringPoint::getAreaName)
                .orderByAsc(MonitoringPoint::getSerialNumber);
        //配置的点
        List<MonitoringPoint> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        AuditRecord auditRecord = this.auditRecordService.getEffectByAuditorName(factoryAreaId, auditorName);
        List<PointAuditRecord> pointAuditRecords = auditRecord.getPointAuditRecords();
        Map<Integer, PARStatusEnum> pointStatusMap = pointAuditRecords.stream()
                .collect(Collectors.toMap(PointAuditRecord::getPointId, PointAuditRecord::getStatus));
        List<MonitoringPointAuditVo> result = list.stream().map(monitoringPoint -> {
            MonitoringPointAuditVo pointVo = new MonitoringPointAuditVo();
            BeanUtils.copyProperties(monitoringPoint, pointVo);
            pointVo.setAuditRecordId(auditRecord.getId());
            pointVo.setStatus(Optional.ofNullable(pointStatusMap.remove(monitoringPoint.getId()))
                    .orElse(PARStatusEnum.SUBMIT_NOT_EXIST));
            return pointVo;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(pointStatusMap)) {
            log.info("exist deleted AuditRecord PointId:Status {}", pointStatusMap);
            // 删除不存在的数据
            pointStatusMap.forEach((key, value) -> {
                LambdaUpdateWrapper<PointAuditRecord> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(PointAuditRecord::getAuditRecordId, auditRecord.getId())
                        .eq(PointAuditRecord::getPointId, key)
                        .set(PointAuditRecord::getIsDeleted, true);
                this.pointAuditRecordService.update(updateWrapper);
            });
        }
        return result;
    }

    @Override
    public List<MonitoringItem> listMonitoringItemByPointId(Integer id) {
        LambdaQueryWrapper<MonitoringItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringItem::getMonitoringPointId, id);
        return this.monitoringItemService.list(wrapper);
    }

    @Override
    public PointAuditRecord getPointRecordStatus(Integer auditRecordId, Integer pointId) {
        return auditRecordService.getPointRecordStatus(auditRecordId, pointId);
    }

    @Override
    public void submitAudit(AuditResultDto resultDto) {
        //检查
        MonitoringPoint monitoringPoint = this.getById(resultDto.getPointId());
        if (monitoringPoint.getItemCount() != resultDto.getItemList().size()) {
            log.info("monitoringPoint.itemCount != resultDto.getItemList()");
            Asserts.fail(ResultCode.SUBMIT_INCOMPLETE);
        }
        //保存数据
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getPointId, resultDto.getPointId())
                .eq(PointAuditRecord::getAuditRecordId, resultDto.getAuditRecordId())
                .eq(PointAuditRecord::getIsDeleted, 0L);
        PointAuditRecord pointAuditRecord = this.pointAuditRecordService.getOne(wrapper, false);
        if (pointAuditRecord == null) {
            pointAuditRecord = new PointAuditRecord();
        }
        //else {
            //if (pointAuditRecord.getStatus() == PARStatusEnum.SUBMIT_SCORE || pointAuditRecord.getStatus() == PARStatusEnum.SUBMITTED) {
            //    return;
            //}
        //}
        setUnqualified(resultDto, pointAuditRecord);
        pointAuditRecord.setAuditRecordId(resultDto.getAuditRecordId());
        LambdaQueryWrapper<PointAuditRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointAuditRecord::getPointId, resultDto.getPointId())
                .eq(PointAuditRecord::getStatus, PARStatusEnum.SUBMITTED.getCode())
                .orderByDesc(PointAuditRecord::getId)
                .last("limit 1");
        PointAuditRecord lastRecord = this.pointAuditRecordService.getOne(queryWrapper);
        log.info("lastRecord--->{}", lastRecord);
        setKeepScore(pointAuditRecord, lastRecord);
        pointAuditRecord.setStatus(PARStatusEnum.SUBMIT_RESULT);
        pointAuditRecord.setPointId(resultDto.getPointId());
        pointAuditRecord.setAreaName(monitoringPoint.getAreaName());
        pointAuditRecord.setSerialNumber(monitoringPoint.getSerialNumber());
        pointAuditRecord.setAuditor(monitoringPoint.getAuditor());
        pointAuditRecord.setItemCount(monitoringPoint.getItemCount());
        pointAuditRecord.setItemCountTotal(0);
        pointAuditRecord.setItemList(resultDto.getItemList());
        pointAuditRecord.setIsDeleted(0L);
        BigDecimal imageScore = BigDecimal.valueOf(pointAuditRecord.getItemCount() - pointAuditRecord.getUnqualifiedItemList().size())
                .divide(BigDecimal.valueOf(pointAuditRecord.getItemCount()), 2, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(70));
        pointAuditRecord.setImageScore(imageScore);
        this.pointAuditRecordService.saveOrUpdate(pointAuditRecord);
        log.info("saveOrUpdate pointAuditRecord--->{}", pointAuditRecord);
    }

    @Override
    public void updateAudit(AuditResultDto resultDto) {
        PointAuditRecord pointAuditRecord = getPointAuditRecord(resultDto.getPointId(), resultDto.getAuditRecordId());
        if (pointAuditRecord.getItemList().size() != resultDto.getItemList().size()) {
            log.info("pointAuditRecord.getItemList != resultDto.getItemList");
            Asserts.fail(ResultCode.SUBMIT_INCOMPLETE);
        }

        setUnqualified(resultDto, pointAuditRecord);
        pointAuditRecord.setItemList(resultDto.getItemList());
        PointAuditRecord lastRecord = this.pointAuditRecordService.getById(pointAuditRecord.getBeforeId());
        setKeepScore(pointAuditRecord, lastRecord);

        BigDecimal imageScore = BigDecimal.valueOf(pointAuditRecord.getUnqualifiedItemList().size())
                .divide(BigDecimal.valueOf(pointAuditRecord.getItemCount()), 2, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(70));
        pointAuditRecord.setImageScore(BigDecimal.valueOf(70).subtract(imageScore));
        pointAuditRecord.setTotalScore(pointAuditRecord.getImageScore()
                .add(BigDecimal.valueOf(pointAuditRecord.getPositionScore() + pointAuditRecord.getCleanScore() + pointAuditRecord.getKeepScore())));
        this.pointAuditRecordService.updateById(pointAuditRecord);
        log.info("update pointAuditRecord--->{}", pointAuditRecord);
        //修改引用这次记录计算keepScore的记录
        LambdaQueryWrapper<PointAuditRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointAuditRecord::getBeforeId, pointAuditRecord.getId());
        List<PointAuditRecord> list = this.pointAuditRecordService.list(queryWrapper);
        list.forEach(auditRecord -> {
            log.info("associate PointAuditRecord --->{}", auditRecord);
            setKeepScore(auditRecord, pointAuditRecord);
            BigDecimal totalScore = auditRecord.getImageScore()
                    .add(BigDecimal.valueOf(auditRecord.getPositionScore() + auditRecord.getCleanScore() + auditRecord.getKeepScore()));
            auditRecord.setTotalScore(totalScore);
            this.pointAuditRecordService.updateById(auditRecord);
            log.info("update associate PointAuditRecord --->{}", auditRecord);
        });
    }

    private void setKeepScore(PointAuditRecord pointAuditRecord, PointAuditRecord lastRecord) {
        Set<Integer> lastItem = Optional.ofNullable(lastRecord)
                .map(PointAuditRecord::getUnqualifiedItemList)
                .orElse(Collections.emptySet());
        pointAuditRecord.setBeforeId(Optional.ofNullable(lastRecord)
                .map(PointAuditRecord::getId).orElse(0));
        if (lastItem.isEmpty()) {
            pointAuditRecord.setKeepScore(10);
        } else {
            double lastSize = lastItem.size();
            lastItem.retainAll(pointAuditRecord.getUnqualifiedItemList());
            double repeatSize = lastItem.size();
            BigDecimal rate = BigDecimal.valueOf(repeatSize).divide(BigDecimal.valueOf(lastSize), RoundingMode.CEILING);
            if (rate.compareTo(BigDecimal.valueOf(0.2)) > 0) {
                pointAuditRecord.setKeepScore(0);
            } else if (rate.compareTo(BigDecimal.ZERO) > 0) {
                pointAuditRecord.setKeepScore(5);
            } else {
                pointAuditRecord.setKeepScore(10);
            }
        }
    }

    private void setUnqualified(AuditResultDto resultDto, PointAuditRecord pointAuditRecord) {
        List<ItemResult> unqualified = new ArrayList<>();
        resultDto.getItemList().forEach(item -> {
            if (Boolean.FALSE.equals(item.getQualified())) {
                unqualified.add(item);
            }
        });
        Set<Integer> unqualifiedItemList = unqualified.stream().map(ItemResult::getId).collect(Collectors.toSet());
        pointAuditRecord.setUnqualifiedItemList(unqualifiedItemList);
        Set<String> urlList = unqualified.stream().map(ItemResult::getImage).filter(StringUtils::hasText).collect(Collectors.toSet());
        pointAuditRecord.setUnqualifiedUrlList(urlList);
    }

    @Override
    public void updateScore(AuditScoreDto scoreDto) {
        PointAuditRecord pointAuditRecord = getPointAuditRecord(scoreDto.getPointId(), scoreDto.getAuditRecordId());
        pointAuditRecord.setPositionScore(scoreDto.getPositionScore());
        pointAuditRecord.setPositionRemark(scoreDto.getPositionRemark());
        pointAuditRecord.setPositionImage(scoreDto.getPositionImage());
        pointAuditRecord.setCleanScore(scoreDto.getCleanScore());
        pointAuditRecord.setCleanRemark(scoreDto.getCleanRemark());
        pointAuditRecord.setCleanImage(scoreDto.getCleanImage());
        BigDecimal totalScore = pointAuditRecord.getImageScore()
                .add(BigDecimal.valueOf(pointAuditRecord.getPositionScore() + pointAuditRecord.getCleanScore() + pointAuditRecord.getKeepScore()));
        pointAuditRecord.setTotalScore(totalScore);
        this.pointAuditRecordService.updateById(pointAuditRecord);
    }

    @Override
    public void submitScore(AuditScoreDto scoreDto) {
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getPointId, scoreDto.getPointId())
                .eq(PointAuditRecord::getAuditRecordId, scoreDto.getAuditRecordId())
                .eq(PointAuditRecord::getIsDeleted, 0L);
        PointAuditRecord pointAuditRecord = this.pointAuditRecordService.getOne(wrapper, false);
        if (pointAuditRecord == null) {
            Asserts.fail(ResultCode.SUBMIT_INCOMPLETE);
        } else {
            if (pointAuditRecord.getStatus() == PARStatusEnum.SUBMITTED) {
                return;
            }
        }
        pointAuditRecord.setPositionScore(scoreDto.getPositionScore());
        pointAuditRecord.setPositionRemark(scoreDto.getPositionRemark());
        pointAuditRecord.setPositionImage(scoreDto.getPositionImage());
        pointAuditRecord.setCleanScore(scoreDto.getCleanScore());
        pointAuditRecord.setCleanRemark(scoreDto.getCleanRemark());
        pointAuditRecord.setCleanImage(scoreDto.getCleanImage());
        pointAuditRecord.setStatus(PARStatusEnum.SUBMIT_SCORE);
        BigDecimal totalScore = pointAuditRecord.getImageScore()
                .add(BigDecimal.valueOf(pointAuditRecord.getPositionScore() + pointAuditRecord.getCleanScore() + pointAuditRecord.getKeepScore()));
        pointAuditRecord.setTotalScore(totalScore);
        this.pointAuditRecordService.updateById(pointAuditRecord);
    }

    private PointAuditRecord getPointAuditRecord(final Integer pointId , final Integer auditRecordId) {
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getPointId, pointId)
                .eq(PointAuditRecord::getAuditRecordId, auditRecordId)
                .eq(PointAuditRecord::getIsDeleted, 0L);
        PointAuditRecord pointAuditRecord = this.pointAuditRecordService.getOne(wrapper, false);
        log.info("getPointAuditRecord--->{}", pointAuditRecord);
        if (pointAuditRecord == null) {
            Asserts.fail(ResultCode.NO_DATA);
        }
        if (pointAuditRecord.getStatus() != PARStatusEnum.SUBMITTED) {
            Asserts.fail(ResultCode.NO_SUBMIT_COMPLETE);
        }
        return pointAuditRecord;
    }

    @Override
    @Transactional
    public void submit(Integer factoryAreaId, String username) {
        LambdaQueryWrapper<AuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditRecord::getFactoryAreaId, factoryAreaId)
                .eq(AuditRecord::getAuditorName, username)
                .eq(AuditRecord::getStatus, AuditRecordStatusEnum.RECORDING.getCode())
                .last("limit 1");
        AuditRecord auditRecord = this.auditRecordService.getOne(wrapper);
        if (auditRecord == null) {
            log.info("AuditRecord is null");
            Asserts.fail(ResultCode.NO_DATA);
        }
        LambdaQueryWrapper<MonitoringPoint> pointWrapper = new LambdaQueryWrapper<>();
        pointWrapper.eq(MonitoringPoint::getFactoryAreaId, factoryAreaId);
        List<MonitoringPoint> monitoringPoints = this.list(pointWrapper);
        Set<Integer> pointSet = monitoringPoints.stream().map(MonitoringPoint::getId).collect(Collectors.toSet());
        List<PointAuditRecord> pointAuditRecords = this.auditRecordService.listPointAuditRecord(auditRecord.getId());
        pointAuditRecords.forEach(pointAuditRecord -> {
            if (pointAuditRecord.getStatus() != PARStatusEnum.SUBMIT_SCORE) {
                Asserts.fail(ResultCode.SUBMIT_INCOMPLETE);
            }
            pointSet.remove(pointAuditRecord.getPointId());
            pointAuditRecord.setStatus(PARStatusEnum.SUBMITTED);
            this.pointAuditRecordService.updateById(pointAuditRecord);
        });
        if (!pointSet.isEmpty()) {
            Asserts.fail(ResultCode.SUBMIT_INCOMPLETE);
        } 
        auditRecord.setStatus(AuditRecordStatusEnum.SUBMIT);
        auditRecord.setCompleteDate(LocalDate.now());
        auditRecord.setUniqueSign(System.currentTimeMillis());
        this.auditRecordService.updateById(auditRecord);
        Map<String, List<PointAuditRecord>> auditGroup = pointAuditRecords.stream().collect(Collectors.groupingBy(PointAuditRecord::getAuditor));
        auditGroup.forEach((audit, points) -> {
            LambdaQueryWrapper<AuditResult> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuditResult::getAuditor, audit)
                    .eq(AuditResult::getFactoryAreaId, auditRecord.getFactoryAreaId())
                    .orderByDesc(AuditResult::getId)
                    .last("limit 1");
            AuditResult lastResult = this.auditResultService.getOne(queryWrapper);
            AuditResult auditResult = new AuditResult();
            auditResult.setAuditRecordId(auditRecord.getId());
            auditResult.setFactoryAreaId(auditRecord.getFactoryAreaId());
            auditResult.setFactoryAreaName(auditRecord.getFactoryAreaName());
            auditResult.setCompleteDate(auditRecord.getCompleteDate());
            auditResult.setAuditorName(username);
            auditResult.setAuditor(audit);
            Double positionScore = points.stream().collect(Collectors.averagingDouble(PointAuditRecord::getPositionScore));
            Double cleanScore = points.stream().collect(Collectors.averagingDouble(PointAuditRecord::getCleanScore));
            List<ItemResult> unqualified = new ArrayList<>();
            points.forEach(point -> {
                point.getItemList().forEach(item -> {
                    if (Boolean.FALSE.equals(item.getQualified())) {
                        unqualified.add(item);
                    }
                });
            });

            auditResult.setItemCountTotal(points.stream().mapToInt(PointAuditRecord::getItemCount).sum());
            auditResult.setPositionScore(BigDecimal.valueOf(positionScore));
            auditResult.setCleanScore(BigDecimal.valueOf(cleanScore));
            Set<Integer> unqualifiedItemList = unqualified.stream().map(ItemResult::getId).collect(Collectors.toSet());
            auditResult.setUnqualifiedItemList(unqualifiedItemList);
            List<String> collect = unqualified.stream().map(ItemResult::getImage).filter(StringUtils::hasText).collect(Collectors.toList());
            auditResult.setUnqualifiedUrlList(collect);
            Set<Integer> lastItem = Optional.ofNullable(lastResult).map(AuditResult::getUnqualifiedItemList).orElse(Collections.emptySet());
            if (lastItem.isEmpty()) {
                auditResult.setKeepScore(BigDecimal.TEN);
            } else {
                double lastSize = lastItem.size();
                lastItem.retainAll(unqualifiedItemList);
                double repeatSize = lastItem.size();
                BigDecimal rate = BigDecimal.valueOf(repeatSize).divide(BigDecimal.valueOf(lastSize), RoundingMode.CEILING);
                if (rate.compareTo(BigDecimal.valueOf(0.2)) > 0) {
                    auditResult.setKeepScore(BigDecimal.ZERO);
                } else if (rate.compareTo(BigDecimal.ZERO) > 0) {
                    auditResult.setKeepScore(BigDecimal.valueOf(5));
                } else {
                    auditResult.setKeepScore(BigDecimal.TEN);
                }
            }
            this.auditResultService.save(auditResult);
        });
    }
}