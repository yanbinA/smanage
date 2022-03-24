package com.temple.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "工厂平面图")
public class FactoryPlanVo {
    @Schema(description = "id")
    private Integer id;

    /**
     * 平面图地址
     */
    @Schema(description = "平面图地址")
    @NotBlank
    private String url;
}
