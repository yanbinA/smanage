package com.temple.manage.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "检查项结果")
public class ItemResult implements Serializable {
    private static final long serialVersionUID = -8658302488081655548L;
    @Schema(description = "检查项id")
    @NotNull
    private Integer id;

    /**
     * 检测项编号
     */
    @Schema(description = "检测项编号")
    @NotNull
    private String itemNumber;

    @Schema(description = "检测项描述")
    @NotNull
    private String remark;

    @Schema(description = "检测项是否合格")
    @NotNull
    private Boolean qualified;

    @Schema(description = "检测项图片")
    @NotNull
    private String itemUrl;

    @Schema(description = "检测现场图片")
    private String image;
}
