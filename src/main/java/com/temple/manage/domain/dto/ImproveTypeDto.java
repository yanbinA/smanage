package com.temple.manage.domain.dto;

import com.temple.manage.common.validators.group.Modify;
import com.temple.manage.entity.enums.ImproveDepartmentEnum;
import com.temple.manage.entity.enums.ImproverLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 改善类型
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 改善类型
 * @date 2022-01-09 23:09
 * @verison V1.0.0
 */
@Data
@Validated
@Schema(description = "改善类型")
public class ImproveTypeDto implements Serializable {
    private static final long serialVersionUID = -6292408802484572101L;
    @NotNull(groups = Modify.class)
    private Integer id;

    /**
     * 改善类型名称
     */
    @NotBlank
    @Schema(description = "改善类型名称")
    @Length(max = 10, min = 1)
    private String name;

    /**
     * 负责人id
     */
    @NotBlank
    @Schema(description = "负责人id")
    @Length(max = 255, min = 1)
    private String userId;

    /**
     * 部门分类,1-质量部,2-IPPI
     */
    @NotNull
    @Schema(description = "部门分类,1-质量部,2-IPPI")
    private ImproveDepartmentEnum departmentType;

    /**
     * 审批层级,1-主管,2-经理,3-厂长
     */
//    @NotNull
    @Schema(description = "审批层级,1-主管,2-经理,3-厂长")
    private ImproverLevelEnum level = ImproverLevelEnum.MANAGER;
}
