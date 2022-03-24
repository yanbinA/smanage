package com.temple.manage.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 分页数据封装类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.api
 * @description 分页数据封装类
 * @date 2022-01-12 21:21
 * @verison V1.0.0
 */
@Data
@Schema(description = "分页数据")
public class CommonPage<T> {
    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Long current;
    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    private Long size;
    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private Long pages;
    /**
     * 总条数
     */
    @Schema(description = "总条数")
    private Long total;
    /**
     * 分页数据
     */
    @Schema(description = "分页数据")
    private List<T> list;



    /**
     * 将MyBatisPlus分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(IPage<T> pageInfo) {
        CommonPage<T> result = new CommonPage<>();
        result.setPages(pageInfo.getPages());
        result.setCurrent(pageInfo.getCurrent());
        result.setSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());
        return result;
    }
}
