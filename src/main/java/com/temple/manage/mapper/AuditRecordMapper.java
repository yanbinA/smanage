package com.temple.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.temple.manage.domain.vo.AuditRecordVo;
import com.temple.manage.entity.AuditRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author messi
* @description 针对表【s_audit_record(审核记录)】的数据库操作Mapper
* @createDate 2021-12-28 22:11:08
* @Entity com.temple.manage.entity.AuditRecord
*/
public interface AuditRecordMapper extends BaseMapper<AuditRecord> {

    IPage<AuditRecordVo> selectPageVo(Page<AuditRecordVo> page);
}




