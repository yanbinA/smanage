package com.temple.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 检查项目
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 检查项目
 * @date 2021-12-27 21:51
 * @verison V1.0.0
 */
@Data
@Schema(description = "检查项目")
public class MonitoringItemDto implements Serializable {
    private static final long serialVersionUID = -3642880000469820653L;
    @Schema(description = "唯一id")
    private Integer id;

    /**
     * 检测项编号
     */
    @Schema(description = "检测项编号")
    @NotBlank
    @Length(max = 20)
    private String itemNumber;

    /**
     * 检测项描述
     */
    @Schema(description = "检测项描述")
    @NotBlank
    @Length(max = 128)
    private String remark;

    /**
     * 图片
     */
    @Schema(description = "图片")
    @NotBlank
    @Length(max = 250)
    private String itemUrl;
}
