package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 0-提交人,1-待审核,2-同意,3-驳回,4-跳过
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 0-待审核,1-提交人,2-同意,3-驳回,4-跳过
 * @date 2022-01-05 22:23
 * @verison V1.0.0
 */
public enum ImproveProcessEnum implements IEnum{
    IN_APPROVAL(0, "待审核"),
    SUBMITTED(1, "提交人"),
    APPROVED(2, "审批通过"),
    REJECTED(3, "驳回"),
    SKIP(4, "跳过"),
    ;
    @EnumValue
    @JsonValue
    private final int code;

    ImproveProcessEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
