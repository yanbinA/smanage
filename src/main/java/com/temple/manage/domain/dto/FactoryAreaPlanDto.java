package com.temple.manage.domain.dto;

import com.temple.manage.common.validators.group.Modify;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 修改车间平面图
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 修改车间平面图
 * @date 2021-12-25 22:37
 * @verison V1.0.0
 */
@Data
public class FactoryAreaPlanDto {
    @NotNull
    private Integer id;

    /**
     * 车间平面图
     */
    @NotEmpty
    private String planUrl;
}
