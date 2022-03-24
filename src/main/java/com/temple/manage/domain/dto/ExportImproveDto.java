package com.temple.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "导出数据查询类")
public class ExportImproveDto implements Serializable {
    private static final long serialVersionUID = -3640537577784580883L;
    @NotNull
    @Schema(description = "查询开始时间")
    private LocalDate startTime;
    @NotNull
    @Schema(description = "查询结束时间")
    private LocalDate endTime;
    @Schema(description = "部门分类,1-质量部,2-IPPI")
    private Integer departmentType;
    @Schema(description = "改善类型id")
    private Integer improveTypeId;
    @Schema(description = "是否已完成")
    private Boolean finish;
}
