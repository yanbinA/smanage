package com.temple.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema(description = "检查分数")
public class AuditScoreDto implements Serializable {
    private static final long serialVersionUID = 2981353699056007103L;
    @Schema(description = "检查点id")
    @NotNull
    private Integer pointId;

    /**
     * 工厂区域id,s_factory_area.id
     */
    @Schema(description = "车间id")
    @NotNull
    private Integer factoryAreaId;

    @Schema(description = "检查记录id")
    @NotNull
    private Integer auditRecordId;

    /**
     * 位置分数
     */
    @Schema(description = "位置分数")
    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Integer positionScore;

    /**
     * 位置得分备注
     */
    @Schema(description = "位置得分备注")
    private String positionRemark;

    /**
     * 位置得分照片,
     */
    @Schema(description = "位置得分照片")
    private String positionImage;

    /**
     * 清洁度得分
     */
    @Schema(description = "清洁度得分")
    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Integer cleanScore;

    /**
     * 清洁度备注
     */
    @Schema(description = "清洁度备注")
    private String cleanRemark;

    /**
     * 清洁度照片
     */
    @Schema(description = "清洁度照片")
    private String cleanImage;
}
