package com.temple.manage.domain.dto;

import com.temple.manage.domain.ItemResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "检查结果")
public class AuditResultDto implements Serializable {
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

    @Schema(description = "检查项结果")
    @Valid
    @NotNull
    @Size(min = 1, max = 50)
    private List<ItemResult> itemList;
}
