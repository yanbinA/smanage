package com.temple.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "导出数据查询类")
public class ExportAuditRecordDto implements Serializable {
    private static final long serialVersionUID = -3640537577784580883L;
    @NotNull
    @Schema(description = "查询开始时间")
    private LocalDate startTime;
    @NotNull
    @Schema(description = "查询结束时间")
    private LocalDate endTime;
    @Schema(description = "车间id")
    private List<Integer> factoryAreaIds;
    @Schema(description = "审核员名称")
    private List<String> auditorNames;
    @Schema(description = "主管名称")
    private List<String> managerNames;
}
