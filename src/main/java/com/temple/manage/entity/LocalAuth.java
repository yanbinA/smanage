package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @TableName s_local_auth
 */
@TableName(value ="s_local_auth")
@Data
public class LocalAuth implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String phoneNo;

    /**
     * 
     */
    private String mailbox;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}