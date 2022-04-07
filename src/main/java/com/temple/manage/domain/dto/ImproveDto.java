package com.temple.manage.domain.dto;

import com.temple.manage.common.validators.group.Modify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "合理建议")
public class ImproveDto implements Serializable {
    private static final long serialVersionUID = 1030420305503708384L;
    @NotNull(groups = Modify.class)
    private Integer id;

    /**
     * 主题
     */
    @NotBlank
    @Length(min = 1, max = 50)
    @Schema(description = "主题")
    private String title;

    /**
     * 描述
     */
    @NotBlank
    @Length(min = 1, max = 255)
    @Schema(description = "主题")
    private String remark;

    /**
     * 措施描述
     */
    @Schema(description = "措施描述")
    private String actionRemark;

    /**
     * 收益描述
     */
    @Schema(description = "收益描述")
    private String proceedRemark;

    /**
     * 是否已完成
     */
    @Schema(description = "是否已完成")
    @NotNull()
    private Boolean finish;

    /**
     * 部门类型id
     */
    @Schema(description = "部门类型id")
    @NotNull(groups = Modify.class)
    private Integer improveTypeId;

    @Schema(description = "审批员userid")
    @NotNull()
    private String adoptUserId;

    @Schema(description = "是否审批通过")
    @NotNull(groups = Modify.class)
    private Boolean adopted;

    @Schema(description = "跟进人id")
    private List<String> followUserIds;
    /**
     * 驳回原因
     */
    @Schema(description = "驳回原因")
    private String rejected;
}
