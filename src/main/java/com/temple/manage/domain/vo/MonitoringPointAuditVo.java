package com.temple.manage.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.temple.manage.entity.enums.PARStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 审核员查询检查点列表返回类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.vo
 * @description 审核员查询检查点列表返回类
 * @date 2021-12-27 21:00
 * @verison V1.0.0
 */
@Data
@Schema(description = "检查点")
public class MonitoringPointAuditVo implements Serializable {
    private static final long serialVersionUID = 1186175373702036161L;
    @Schema(description = "唯一id")
    private Integer id;

    /**
     * 工厂区域id,s_factory_area.id
     */
    @Schema(description = "车间id")
    private Integer factoryAreaId;

    @Schema(description = "检查记录id")
    private Integer auditRecordId;

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

    @Schema(description = "检查点状态")
    private PARStatusEnum status;

    @Schema(description = "检查项列表")
    private List<MonitoringItemVo> itemList;
    /**
     * 位置分数
     */
    @Schema(description = "位置分数")
    private Integer positionScore;

    /**
     * 位置得分备注
     */
    @Schema(description = "位置得分备注")
    private String positionRemark;

    /**
     * 位置得分照片,
     */
    @Schema(description = "位置得分照片")
    private String positionImage;

    /**
     * 清洁度得分
     */
    @Schema(description = "清洁度得分")
    private Integer cleanScore;

    /**
     * 清洁度备注
     */
    @Schema(description = "清洁度备注")
    private String cleanRemark;

    /**
     * 清洁度照片
     */
    @Schema(description = "清洁度照片")
    private String cleanImage;
}
