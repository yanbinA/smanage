package com.temple.manage.common.api;

import java.io.Serializable;

/**
 * <p>
 * 定义返回码接口
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.api
 * @description 定义返回码接口
 * @date 2021-12-24 0:52
 * @verison V1.0.0
 */
public interface IErrorCode extends Serializable {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
