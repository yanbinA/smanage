package com.temple.manage.common.exception;

import com.temple.manage.common.api.IErrorCode;

/**
 * <p>
 * 断言类,用于抛出异常
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.exception
 * @description 断言类, 用于抛出异常
 * @date 2021-12-24 0:50
 * @verison V1.0.0
 */
public class Asserts {
    private Asserts() {
        throw new IllegalStateException("Utility class");
    }

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static ApiException throwException(IErrorCode errorCode) {
        return new ApiException(errorCode);
    }

    public static ApiException throwException(String message) {
        return new ApiException(message);
    }
}
