package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 
 * @TableName s_audit_result
 */
@TableName(value ="s_audit_result", autoResultMap = true)
@Data
public class AuditResult implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer auditRecordId;

    /**
     * 车间id,s_factory_area.id
     */
    private Integer factoryAreaId;

    /**
     * 车间名称
     */
    private String factoryAreaName;

    /**
     * 提交日期
     */
    private LocalDate completeDate;

    /**
     * 审核员名称
     */
    private String auditorName;

    /**
     * 主管名称
     */
    private String auditor;

    /**
     * 主管检测项目总数
     */
    private Integer itemCountTotal;

    /**
     * 位置分数
     */
    private BigDecimal positionScore;

    /**
     * 清洁度得分
     */
    private BigDecimal cleanScore;

    /**
     * 维持得分
     */
    private BigDecimal keepScore;

    /**
     * 不合格的检查项
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Integer> unqualifiedItemList;

    /**
     * 不合格的图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> unqualifiedUrlList;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(value = "modify_time", updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}