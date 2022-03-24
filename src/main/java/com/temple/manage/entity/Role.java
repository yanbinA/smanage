package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @TableName s_role
 */
@TableName(value ="s_role")
@Data
public class Role implements GrantedAuthority, Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    @Override
    public String getAuthority() {
        return this.name;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}