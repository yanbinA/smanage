package com.temple.manage.domain.dto;

import com.temple.manage.domain.ItemResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 整个车间数据一次性提交
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 整个车间数据一次性提交
 * @date 2022-02-26 15:55
 * @verison V1.0.0
 */
@Data
public class FactoryAuditDto {
    /**
     * 工厂区域id,s_factory_area.id
     */
    @Schema(description = "车间id")
    @NotNull
    private Integer factoryAreaId;

    @Schema(description = "检查记录id")
    @NotNull
    private Integer auditRecordId;

    @Valid
    @NotNull
    @Schema(description = "检查点记录")
    private List<AuditResult> list;

    @Data
    public static class AuditResult {
        @Schema(description = "检查点id")
        @NotNull
        private Integer pointId;

        @Schema(description = "检查项结果")
        @Valid
        @NotNull
        @Size(min = 1, max = 50)
        private List<ItemResult> itemList;

        /**
         * 位置分数
         */
        @Schema(description = "位置分数")
        @NotNull
        @Min(value = 0)
        @Max(value = 10)
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
        @NotNull
        @Min(value = 0)
        @Max(value = 10)
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
}
