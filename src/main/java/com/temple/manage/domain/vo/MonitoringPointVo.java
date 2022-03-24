package com.temple.manage.domain.vo;

import com.temple.manage.entity.enums.PARStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 检查点列表返回类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.vo
 * @description 检查点列表返回类
 * @date 2021-12-27 21:00
 * @verison V1.0.0
 */
@Data
@Schema(description = "检查点")
public class MonitoringPointVo implements Serializable {
    private static final long serialVersionUID = 1186175373702036161L;
    @Schema(description = "唯一id")
    private Integer id;

    /**
     * 工厂区域id,s_factory_area.id
     */
    @Schema(description = "车间id")
    private Integer factoryAreaId;

    /**
     * 区域名称
     */
    @Schema(description = "区域名称")
    private String areaName;

    /**
     * 序号
     */
    @Schema(description = "序号")
    private String serialNumber;

    /**
     * 主管名称
     */
    @Schema(description = "主管名称")
    private String auditor;

    /**
     * 检查点位置-横坐标
     */
    @Schema(description = "检查点位置-横坐标")
    private Integer abscissa;

    /**
     * 检查点位置-纵坐标
     */
    @Schema(description = "检查点位置-纵坐标")
    private Integer ordinate;
}
