package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName s_factory_area
 */
@TableName(value ="s_factory_area")
@Data
public class FactoryArea implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车间名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 车间编号
     */
    @TableField(value = "serial_number")
    private String serialNumber;

    /**
     * 车间平面图
     */
    @TableField(value = "plan_url")
    private String planUrl;

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
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}