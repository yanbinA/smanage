package com.temple.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "车间")
public class FactoryAreaVo implements Serializable {
    private static final long serialVersionUID = -5429643213177123888L;
    private Integer id;

    /**
     * 区域名称
     */
    @Schema(description = "车间名称")
    private String name;

    /**
     * 车间编号
     */
    @Schema(description = "车间编号")
    private String serialNumber;

    /**
     * 车间平面图
     */
    @Schema(description = "车间平面图")
    private String planUrl;

    @Schema(description = "检查点数量")
    private Integer count;
}
