package com.temple.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema(description = "检查项目")
public class MonitoringItemVo implements Serializable {
    @Schema(description = "唯一id")
    private Integer id;

    /**
     * 检测项编号
     */
    @Schema(description = "检测项编号")
    private String itemNumber;

    /**
     * 检测项描述
     */
    @Schema(description = "检测项描述")
    private String remark;

    /**
     * 图片
     */
    @Schema(description = "图片")
    private String itemUrl;

    @Schema(description = "检测项是否合格")
    private Boolean qualified;

    @Schema(description = "检测现场图片")
    private String image;

    private static final long serialVersionUID = 7144536078533314558L;
}
