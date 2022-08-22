package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 监测点
 * @TableName s_monitoring_point
 */
@TableName(value ="s_monitoring_point")
@Data
public class MonitoringPoint implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂区域id,s_factory_area.id
     */
    @TableField(value = "factory_area_id")
    private Integer factoryAreaId;

    /**
     * 区域名称
     */
    @TableField(value = "area_name")
    private String areaName;

    /**
     * 序号
     */
    @TableField(value = "serial_number")
    private String serialNumber;

    /**
     * 主管名称
     */
    @TableField(value = "auditor")
    private String auditor;

    /**
     * 检查点位置-横坐标
     */
    @TableField(value = "abscissa")
    private Integer abscissa;

    /**
     * 检查点位置-纵坐标
     */
    @TableField(value = "ordinate")
    private Integer ordinate;

    /**
     * 检测项目数
     */
    @TableField(value = "item_count")
    private Integer itemCount;

    /**
     * 是否删除,1-删除,0-未删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

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