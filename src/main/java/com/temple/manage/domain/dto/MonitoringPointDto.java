package com.temple.manage.domain.dto;

import com.temple.manage.common.validators.group.Modify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 检查点
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 检查点
 * @date 2021-12-27 21:41
 * @verison V1.0.0
 */
@Data
@Schema(description = "检查点")
public class MonitoringPointDto implements Serializable {
    private static final long serialVersionUID = -5733708180031985532L;
    @Schema(description = "唯一id")
    @NotNull(groups = {Modify.class})
    private Integer id;

    /**
     * 工厂区域id,s_factory_area.id
     */
    @Schema(description = "车间id")
    @NotNull
    private Integer factoryAreaId;

    /**
     * 区域名称
     */
    @Schema(description = "区域名称")
    @NotEmpty
    @Length(max = 20)
    private String areaName;

    /**
     * 序号
     */
    @Schema(description = "序号")
    @NotNull
    @Positive
    private Integer serialNumber;

    /**
     * 主管名称
     */
    @Schema(description = "主管名称")
    @NotBlank
    @Length(max = 20)
    private String auditor;

    /**
     * 检查点位置-横坐标
     */
    @Schema(description = "检查点位置-横坐标")
    @NotNull
    private Integer abscissa;

    /**
     * 检查点位置-纵坐标
     */
    @Schema(description = "检查点位置-纵坐标")
    @NotNull
    private Integer ordinate;

    @NotNull
    @Size(min = 1, max = 50)
    private List<MonitoringItemDto> itemList;
}
