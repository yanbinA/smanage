package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 审核记录状态,1-审核中,2-已提交
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 审核记录状态
 * @date 2021-12-28 22:15
 * @verison V1.0.0
 */
public enum AuditRecordStatusEnum implements IEnum{
    RECORDING(1, "审核中"),
    SUBMIT(2, "已提交"),
    ;
    //@EnumValue,是mybatisplus提供的注解枚举属性,用来标记数据库存的值
    @EnumValue
    @JsonValue
    private final int code;

    AuditRecordStatusEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
