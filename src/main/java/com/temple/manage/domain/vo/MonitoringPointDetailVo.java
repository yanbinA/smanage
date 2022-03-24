package com.temple.manage.domain.vo;

import com.temple.manage.entity.MonitoringItem;
import com.temple.manage.entity.enums.PARStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "询检查点详情")
@Data
public class MonitoringPointDetailVo implements Serializable {
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

    private List<MonitoringItemVo> itemList;

    private static final long serialVersionUID = 6601669596147082593L;
}
