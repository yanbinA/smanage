package com.temple.manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.temple.manage.entity.enums.ImproveProcessEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 审批流程
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 审批流程
 * @date 2022-01-11 0:09
 * @verison V1.0.0
 */
@Data
@AllArgsConstructor
@Schema(description = "审批流程")
public class ImproveProcess implements Serializable {
    private static final long serialVersionUID = -6833926337883806457L;
    @Schema(description = "流程人员id")
    private String userId;//"流程人员名称"
    @Schema(description = "流程人员名称")
    private String username;//"流程人员名称"
    @Schema(description = "流程状态,0-待审核,1-提交人,2-同意,3-驳回,4-跳过")
    private ImproveProcessEnum operation;//0-待审核,1-提交人,2-同意,3-驳回,4-跳过
    @Schema(description = "审批时间")
    private LocalDateTime time;
    @Schema(description = "跟进人id", hidden = true)
    @JsonIgnore
    private List<String> followUserIds;
    @Schema(description = "跟进人id", hidden = true)
    @JsonIgnore
    private LocalDate followDate;
}
