package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.temple.manage.common.handler.ItemResultTypeHandler;
import com.temple.manage.domain.ItemResult;
import com.temple.manage.entity.enums.PARStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 检查点审核记录
 * @TableName s_point_audit_record
 */
@TableName(value ="s_point_audit_record", autoResultMap = true)
@Data
public class PointAuditRecord implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * s_audit_record.id
     */
    @TableField(value = "audit_record_id")
    private Integer auditRecordId;

    /**
     * 1-提交检测结果,2-提交评分
     */
    @TableField(value = "status")
    private PARStatusEnum status;

    /**
     * s_monitoring_point.id
     */
    @TableField(value = "point_id")
    private Integer pointId;

    /**
     * 区域名称,s_monitoring_poin.area_name
     */
    @TableField(value = "area_name")
    private String areaName;

    /**
     * 序号,s_monitoring_poin.serial_number
     */
    @TableField(value = "serial_number")
    private String serialNumber;

    /**
     * 主管名称
     */
    @TableField(value = "auditor")
    private String auditor;

    /**
     * 检测项目数
     */
    @TableField(value = "item_count")
    private Integer itemCount;

    /**
     * 主管检测项目总数
     */
    @TableField(value = "item_count_total")
    private Integer itemCountTotal;

    /**
     * 位置分数
     */
    @TableField(value = "position_score")
    private Integer positionScore;

    /**
     * 位置得分备注
     */
    @TableField(value = "position_remark")
    private String positionRemark;

    /**
     * 位置得分照片,
     */
    @TableField(value = "position_image")
    private String positionImage;

    /**
     * 清洁度得分
     */
    @TableField(value = "clean_score")
    private Integer cleanScore;

    /**
     * 清洁度备注
     */
    @TableField(value = "clean_remark")
    private String cleanRemark;

    /**
     * 清洁度照片
     */
    @TableField(value = "clean_image")
    private String cleanImage;

    /**
     * 检测项结果
     */
    @TableField(value = "item_list", javaType = true, typeHandler = ItemResultTypeHandler.class)
    private List<ItemResult> itemList;

    /**
     * 上次检查记录
     */
    private Integer beforeId;
    /**
     * 维持得分
     */
    private Integer keepScore;
    /**
     * 图片得分
     */
    private BigDecimal imageScore;

    /**
     * 总得分
     */
    private BigDecimal totalScore;

    /**
     * 不合格的检查项
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Integer> unqualifiedItemList;

    /**
     * 不合格的图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> unqualifiedUrlList;

    /**
     * 是否删除,0-未删除,1-已删除
     */
    @TableField(value = "is_deleted")
    private Long isDeleted;

    @Version
    private Long version;

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
    private static final long serialVersionUID = 1L;
}