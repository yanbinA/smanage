package com.temple.manage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Schema(description = "可选数据")
public class RecordSelectVo implements Serializable {
    private static final long serialVersionUID = 1798997019563051696L;
    @Schema(description = "可选车间列表")
    private Set<FactoryAreaSelect> factoryAreaList;
    @Schema(description = "可选审核员名称")
    private Set<String> auditorNames;
    @Schema(description = "可选主管名称")
    private Set<String> managerNames;

    @Data
    @Builder
    @Schema(description = "可选车间")
    public static class FactoryAreaSelect implements Serializable{
        private static final long serialVersionUID = 4259799052855764407L;
        @Schema(description = "车间id")
        private Integer factoryAreaId;

        @Schema(description = "车间名称")
        private String name;
    }
}
