package com.temple.manage.common.log;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日志封装类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.domain
 * @description 日志封装类
 * @date 2021-12-24 1:02
 * @verison V1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebLog {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回结果
     */
    private Object result;
}
