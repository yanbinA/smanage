package com.temple.manage.entity.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatusEnum implements IEnum<Integer> {
    DELETE(1, "已激活"),
    ENABLE(2, "未激活");
    @EnumValue
    @JsonValue
    private final int code;
    private final String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }



//    @Override
//    public String toString() {
//        return code + "";
//    }


    @Override
    public String toString() {
        return this.code + "-" + this.name;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
