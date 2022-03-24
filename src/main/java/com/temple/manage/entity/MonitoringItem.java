package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 检测项目
 * @TableName s_monitoring_item
 */
@TableName(value ="s_monitoring_item")
@Data
public class MonitoringItem implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 监测点id,s_monitoring_point.id
     */
    private Integer monitoringPointId;

    /**
     * 检测项编号
     */
    private String itemNumber;

    /**
     * 检测项描述
     */
    private String remark;

    /**
     * 图片
     */
    private String itemUrl;

    /**
     * 是否删除,1-删除,0-未删除
     */
    private Boolean isDeleted;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}