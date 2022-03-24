package com.temple.manage.common.log;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * service日志
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.log
 * @description service日志
 * @date 2022-01-01 16:59
 * @verison V1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceLog {
    private String className;
    private String methodName;
    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;
}
