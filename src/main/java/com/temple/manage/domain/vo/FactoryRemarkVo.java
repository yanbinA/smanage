package com.temple.manage.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@Schema(description = "评分项描述")
public class FactoryRemarkVo implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    @Schema(description = "位置项描述")
    @NotBlank
    private String localRemark;

    @Schema(description = "位置项标题")
    @NotBlank
    private String localTitle;

    /**
     * 
     */
    @Schema(description = "清洁项描述")
    @NotBlank
    private String cleanRemark;

    @Schema(description = "清洁项标题")
    @NotBlank
    private String cleanTitle;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}