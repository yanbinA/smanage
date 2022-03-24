package com.temple.manage.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 检查点状态{@link com.temple.manage.entity.PointAuditRecord}#status,1-提交检测结果,2-提交评分
 * </p>
 *
 * @author messi
 * @package com.temple.manage.entity.enums
 * @description 审核记录状态
 * @date 2021-12-28 22:15
 * @verison V1.0.0
 */
public enum PARStatusEnum implements IEnum{
    SUBMIT_NOT_EXIST(0, "未提交"),
    SUBMIT_RESULT(1, "提交检测结果"),
    SUBMIT_SCORE(2, "提交评分"),
    SUBMITTED(3, "已提交"),
    ;
    //@EnumValue,是mybatisplus提供的注解枚举属性,用来标记数据库存的值
    @EnumValue
    @JsonValue
    private final int code;

    PARStatusEnum(int code, String name) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
