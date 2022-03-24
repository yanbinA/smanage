package com.temple.manage.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "审核记录")
public class AuditRecordVo {
    @Schema(description = "审核记录id")
    private Integer auditRecordId;

    /**
     * 审核员名称
     */
    @Schema(description = "审核员名称")
    private String auditorName;

    /**
     * 车间id,s_factory_area.id
     */
    @Schema(description = "车间id")
    private Integer factoryAreaId;

    /**
     * 车间名称
     */
    @Schema(description = "车间名称")
    private String factoryAreaName;

    @Schema(description = "审核时间")
    private LocalDateTime modifyTime;
    @Schema(description = "平均分值")
    private BigDecimal avgScore;
}
