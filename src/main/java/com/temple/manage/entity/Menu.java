package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName s_menu
 */
@TableName(value ="s_menu")
@Data
public class Menu implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer parentId;

    /**
     * 
     */
    private Boolean enabled;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}