package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.temple.manage.entity.enums.ImproveDepartmentEnum;
import com.temple.manage.entity.enums.ImproverLevelEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 改善类型
 * @TableName s_improve_type
 */
@TableName(value ="s_improve_type")
@Data
public class ImproveType implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 改善类型
     */
    @TableField(value = "name")
    private String name;

    /**
     * 负责人id
     */
    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "user_name")
    private String userName;

    /**
     * 部门分类,1-质量部,2-IPPI
     */
    @TableField(value = "department_type")
    private ImproveDepartmentEnum departmentType;

    /**
     * 审批层级,1-主管,2-经理,3-厂长
     */
    @TableField(value = "level")
    private ImproverLevelEnum level;

    /**
     * 
     */
    @TableField(value = "version")
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
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}