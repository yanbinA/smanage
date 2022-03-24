package com.temple.manage.common.exception;

import com.temple.manage.common.api.IErrorCode;

/**
 * <p>
 * 自定义API异常
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.exception
 * @description 自定义API异常
 * @date 2021-12-24 0:51
 * @verison V1.0.0
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -1089674007083711540L;
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
