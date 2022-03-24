package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActiveEnum {
    //1-已激活; 2-未激活,3-待激活
    ACTIVED(1, "已激活"),
    NOT_ACTIVE(2, "未激活"),
    ACTIVE(3, "待激活"),
    ;
    @EnumValue
    @JsonValue
    private final int code;

    ActiveEnum(int code, String name) {
        this.code = code;
    }
}
