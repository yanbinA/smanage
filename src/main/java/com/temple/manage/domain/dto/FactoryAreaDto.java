package com.temple.manage.domain.dto;

import com.temple.manage.common.validators.group.Modify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema(description = "车间")
public class FactoryAreaDto implements Serializable {
    @NotNull(groups = Modify.class)
    private Integer id;
    /**
     * 区域名称
     */
    @NotBlank
    @Length(max = 50)
    @Schema(description = "车间名称")
    private String name;

    /**
     * 车间编号
     */
    @NotBlank
    @Length(max = 50)
    @Schema(description = "车间编号")
    private String serialNumber;

    /**
     * 车间平面图
     */
    @Schema(description = "车间平面图")
    private String planUrl;

    private static final long serialVersionUID = -5351895994622359493L;
}
