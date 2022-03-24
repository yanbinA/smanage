package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum DynamicsEnum implements IEnum<Integer> {
    //1.已活跃,2:未活跃,3:待活跃
    ACTIVED(1, "已活跃"),
    NOT_ACTIVE(2, "未活跃"),
    ACTIVE(3, "待活跃")
    ;
    private final int code;

    DynamicsEnum(int code, String name) {
        this.code = code;
    }
    @Override
    public Integer getValue() {
        return code;
    }

    @Override
    public String toString() {
        return code + "";
    }
}
