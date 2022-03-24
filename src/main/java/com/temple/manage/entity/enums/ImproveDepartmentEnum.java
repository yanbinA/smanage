package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 部门分类,1-质量部,2-精益
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 部门分类, 1-质量部,2-精益
 * @date 2022-01-05 22:13
 * @verison V1.0.0
 */
public enum ImproveDepartmentEnum implements IEnum{
    QUALITY(1, "质量"),
    LEAN(2, "精益"),
    ;
    @EnumValue
    @JsonValue
    private final int code;

    ImproveDepartmentEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
