package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 1-审批中,2-审核通过,3-驳回
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 1-审批中,2-审批通过,3-驳回
 * @date 2022-01-05 22:20
 * @verison V1.0.0
 */
public enum ImproveStatusEnum implements IEnum{
    IN_APPROVAL(1, "审批中"),
    APPROVED(2, "审批通过"),
    REJECTED(3, "驳回"),
    ;
    @EnumValue
    @JsonValue
    private final int code;

    ImproveStatusEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
