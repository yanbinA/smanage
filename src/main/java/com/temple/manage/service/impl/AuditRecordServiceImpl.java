package com.temple.manage.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.domain.vo.AuditRecordVo;
import com.temple.manage.entity.AuditRecord;
import com.temple.manage.entity.FactoryArea;
import com.temple.manage.entity.PointAuditRecord;
import com.temple.manage.entity.enums.AuditRecordStatusEnum;
import com.temple.manage.mapper.AuditRecordMapper;
import com.temple.manage.service.AuditRecordService;
import com.temple.manage.service.FactoryAreaService;
import com.temple.manage.service.PointAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
* @author messi
* @description 针对表【s_audit_record(审核记录)】的数据库操作Service实现
* @createDate 2021-12-28 22:11:08
*/
@Service
@Slf4j
public class AuditRecordServiceImpl extends ServiceImpl<AuditRecordMapper, AuditRecord>
    implements AuditRecordService{
    private final PointAuditRecordService pointAuditRecordService;
    private final FactoryAreaService factoryAreaService;

    public AuditRecordServiceImpl(PointAuditRecordService pointAuditRecordService,
                                  FactoryAreaService factoryAreaService) {
        this.pointAuditRecordService = pointAuditRecordService;
        this.factoryAreaService = factoryAreaService;
    }

    @Override
    public AuditRecord getEffectByAuditorName(Integer factoryAreaId, String auditorName) {
        log.info("get effect with AuditorName and factoryAreaId,{}, {}", auditorName, factoryAreaId);
        LambdaQueryWrapper<AuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditRecord::getFactoryAreaId, factoryAreaId)
                .eq(AuditRecord::getAuditorName, auditorName)
                .eq(AuditRecord::getStatus, AuditRecordStatusEnum.RECORDING.getCode())
                .last("limit 1");
        AuditRecord auditRecord = this.getOne(wrapper);
        if (auditRecord == null) {
            log.info("not exist record under RECORDING");
            FactoryArea factoryArea = factoryAreaService.getById(factoryAreaId);
            if (factoryArea == null) {
                log.info("not exist factoryAreaId");
                Asserts.fail("车间不存在,请刷新后重试");
            }
            auditRecord = new AuditRecord();
            auditRecord.setAuditorName(auditorName);
            auditRecord.setStatus(AuditRecordStatusEnum.RECORDING);
            auditRecord.setFactoryAreaId(factoryAreaId);
            auditRecord.setFactoryAreaName(factoryArea.getName());
            auditRecord.setFactoryAreaSerialNumber(factoryArea.getSerialNumber());
            auditRecord.setUniqueSign(1L);
            try {
                this.save(auditRecord);
                auditRecord.setPointAuditRecords(Collections.emptyList());
                return auditRecord;
            } catch (DuplicateKeyException e) {
                log.info("exist record,{}", auditRecord);
                return this.getEffectByAuditorName(factoryAreaId, auditorName);
            }
        }
        auditRecord.setPointAuditRecords(this.listPointAuditRecord(auditRecord.getId()));
        return auditRecord;
    }

    @Override
    public List<PointAuditRecord> listPointAuditRecord(Integer auditRecordId) {
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getAuditRecordId, auditRecordId)
                .eq(PointAuditRecord::getIsDeleted, false);
        return Optional.ofNullable(this.pointAuditRecordService.list(wrapper))
                .orElse(Collections.emptyList());
    }

    @Override
    public PointAuditRecord getPointRecordStatus(Integer auditRecordId, Integer pointId) {
        LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointAuditRecord::getAuditRecordId, auditRecordId)
                .eq(PointAuditRecord::getPointId, pointId)
                .eq(PointAuditRecord::getIsDeleted, false)
                .last("limit 1");
        return this.pointAuditRecordService.getOne(wrapper);
    }

    @Override
    public IPage<AuditRecordVo> selectPageVo(Page<AuditRecordVo> page) {
        IPage<AuditRecordVo> pageVo = baseMapper.selectPageVo(page);
        pageVo.getRecords().forEach(auditRecord -> {
            LambdaQueryWrapper<PointAuditRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PointAuditRecord::getAuditRecordId, auditRecord.getAuditRecordId());
            double avg = pointAuditRecordService.list(wrapper)
                    .stream().map(PointAuditRecord::getTotalScore)
                    .collect(Collectors.averagingDouble(BigDecimal::doubleValue));
            auditRecord.setAvgScore(BigDecimal.valueOf(avg));
        });
        return pageVo;
    }
}




