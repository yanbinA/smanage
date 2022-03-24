package com.temple.manage.domain.vo;


import com.temple.manage.domain.ItemResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "检查点审核记录")
public class PointAuditRecordVo implements Serializable {
    private static final long serialVersionUID = 6193575271363227253L;
    @Schema(description = "检查点审核记录id")
    private Integer id;

    /**
     * s_audit_record.id
     */
    @Schema(description = "审核记录id")
    private Integer auditRecordId;


    /**
     * s_monitoring_point.id
     */
    @Schema(description = "检查点id")
    private Integer pointId;

    /**
     * 区域名称,s_monitoring_poin.area_name
     */
    @Schema(description = "区域名称")
    private String areaName;

    /**
     * 序号,s_monitoring_poin.serial_number
     */
    @Schema(description = "序号")
    private String serialNumber;

    /**
     * 主管名称
     */
    @Schema(description = "主管名称")
    private String auditor;

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

    /**
     * 检测项结果
     */
    @Schema(description = "检测项结果")
    private List<ItemResult> itemList;

    /**
     * 总得分
     */
    @Schema(description = "总得分")
    private BigDecimal totalScore;
}
