package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.temple.manage.entity.enums.AuditRecordStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 审核记录
 * @TableName s_audit_record
 */
@TableName(value ="s_audit_record")
@Data
public class AuditRecord implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 审核员名称
     */
    @TableField(value = "auditor_name")
    private String auditorName;

    /**
     * 审核状态,1-审核中,2-已提交
     */
    @TableField(value = "status")
    private AuditRecordStatusEnum status;

    /**
     * 车间id,s_factory_area.id
     */
    @TableField(value = "factory_area_id")
    private Integer factoryAreaId;

    /**
     * 车间名称
     */
    @TableField(value = "factory_area_name")
    private String factoryAreaName;

    /**
     * 车间编号
     */
    @TableField(value = "factory_area_serial_number")
    private String factoryAreaSerialNumber;

    /**
     * 提交日期
     */
    @TableField(value = "complete_date")
    private LocalDate completeDate;

    /**
     * 是否删除,0-未删除,1-已删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 唯一标记 1-唯一数据,timestamp-不需要唯一
     */
    @TableField(value = "unique_sign")
    private Long uniqueSign;
    /**
     * 
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(value = "modify_time", updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private List<PointAuditRecord> pointAuditRecords;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}