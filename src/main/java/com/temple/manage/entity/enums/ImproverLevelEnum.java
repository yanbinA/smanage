package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 审批层级,1-主管,2-经理,3-厂长
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 审批层级,1-主管,2-经理,3-厂长
 * @date 2022-01-05 22:10
 * @verison V1.0.0
 */
public enum ImproverLevelEnum implements IEnum {
    SUPERVISOR(1, "主管"),
    MANAGER(2, "经理"),
    DIRECTOR(3, "厂长"),
    ;
    @EnumValue
    @JsonValue
    private final int code;

    ImproverLevelEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
