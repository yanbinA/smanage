package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 用户性别枚举
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 用户性别枚举
 * @date 2021-12-20 22:14
 * @verison V1.0.0
 */
public enum UserGenderEnum {
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;
    @EnumValue
    @JsonValue
    private final int code;
    private final String name;

    UserGenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
