package com.temple.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.temple.manage.domain.vo.AuditRecordVo;
import com.temple.manage.entity.AuditRecord;
import com.temple.manage.entity.PointAuditRecord;

import java.util.List;

/**
* @author messi
* @description 针对表【s_audit_record(审核记录)】的数据库操作Service
* @createDate 2021-12-28 22:11:08
*/
public interface AuditRecordService extends IService<AuditRecord> {

    /**
     * 查询auditorName在factoryAreaId下的有效任务
     * @param factoryAreaId factoryAreaId
     * @param auditorName auditorName
     */
    AuditRecord getEffectByAuditorName(Integer factoryAreaId, String auditorName);

    /**
     * 查询auditRecordId下的PointAuditRecord
     * @param auditRecordId auditRecordId
     * @return List<PointAuditRecord>
     */
    List<PointAuditRecord> listPointAuditRecord(Integer auditRecordId);

    PointAuditRecord getPointRecordStatus(Integer auditorRecordId, Integer pointId);

    IPage<AuditRecordVo> selectPageVo(Page<AuditRecordVo> page);
}
