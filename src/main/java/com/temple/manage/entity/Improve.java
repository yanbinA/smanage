package com.temple.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.temple.manage.common.handler.ImproveProcessTypeHandler;
import com.temple.manage.entity.enums.ImproveDepartmentEnum;
import com.temple.manage.entity.enums.ImproveStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @TableName s_improve
 */
@TableName(value ="s_improve")
@Data
@Schema(description = "建议详情")
public class Improve implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 提出人id
     */
    @TableField(value = "user_id")
    @Schema(description = "提出人id")
    private String userId;

    /**
     * 提出人name
     */
    @TableField(value = "user_name")
    @Schema(description = "提出人name")
    private String userName;

    /**
     * 提出人部门id
     */
    @TableField(value = "department")
    @Schema(description = "提出人部门id", hidden = true)
    private Long department;

    /**
     * 1-审批中,2-审核通过,3-驳回
     */
    @TableField(value = "status")
    @Schema(description = "审批状态,1-审批中,2-审核通过,3-驳回")
    private ImproveStatusEnum status;

    /**
     * 主题
     */
    @TableField(value = "title")
    @Schema(description = "主题")
    private String title;

    /**
     * 描述
     */
    @TableField(value = "remark")
    @Schema(description = "描述")
    private String remark;

    /**
     * 措施描述
     */
    @TableField(value = "action_remark")
    @Schema(description = "措施描述")
    private String actionRemark;

    /**
     * 收益描述
     */
    @TableField(value = "proceed_remark")
    @Schema(description = "收益描述")
    private String proceedRemark;

    /**
     * 是否已完成
     */
    @TableField(value = "finish")
    @Schema(description = "是否已完成")
    private Boolean finish;

    /**
     * 改善类型id
     */
    @TableField(value = "improve_type_id")
    @Schema(description = "改善类型id")
    private Integer improveTypeId;

    /**
     * 部门分类,1-质量部,2-IPPI
     */
    @TableField(value = "department_type")
    @Schema(description = "部门分类", hidden = true)
    private ImproveDepartmentEnum departmentType;

    /**
     * 改善类型名称
     */
    @Schema(description = "改善类型名称")
    @TableField(value = "improve_name")
    private String improveName;

    /**
     * 审核流程
     */
    @Schema(description = "审核流程")
    @TableField(value = "process", typeHandler = ImproveProcessTypeHandler.class)
    private List<ImproveProcess> process;

    /**
     * 下一个审批人id
     */
    @Schema(description = "当前审批人id")
    @TableField(value = "next_user_id")
    private String nextUserId;

    /**
     * 下一个审批人名称
     */
    @Schema(description = "当前审批人名称")
    @TableField(value = "next_user_name")
    private String nextUserName;

    /**
     * 驳回原因
     */
    @Schema(description = "驳回原因")
    @TableField(value = "rejected")
    private String rejected;

    /**
     * 
     */
    @TableField(value = "version")
    @Schema(description = "版本号", hidden = true)
    private Long version;

    /**
     * 
     */
    @TableField(value = "create_time")
    @Schema(description = "创建时间", hidden = true)
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(value = "modify_time")
    @Schema(description = "修改时间", hidden = true)
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}