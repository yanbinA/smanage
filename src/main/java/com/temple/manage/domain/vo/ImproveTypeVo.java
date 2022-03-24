package com.temple.manage.domain.vo;

import com.temple.manage.entity.enums.ImproveDepartmentEnum;
import com.temple.manage.entity.enums.ImproverLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
@Schema(description = "改善类型")
public class ImproveTypeVo implements Serializable {
    private static final long serialVersionUID = -6292408802484572101L;
    private Integer id;

    /**
     * 改善类型名称
     */
    @Schema(description = "改善类型名称")
    private String name;

    /**
     * 负责人id
     */
    @Schema(description = "负责人id")
    private String userId;
    @Schema(description = "负责人名称")
    private String userName;

    /**
     * 部门分类,1-质量部,2-IPPI
     */
    @Schema(description = "部门分类,1-质量部,2-IPPI")
    private ImproveDepartmentEnum departmentType;

    /**
     * 审批层级,1-主管,2-经理,3-厂长
     */
    @Schema(description = "审批层级,1-主管,2-经理,3-厂长")
    private ImproverLevelEnum level;
}
