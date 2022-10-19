package com.temple.manage.common.exception;

import com.temple.manage.common.api.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.exception
 * @description 全局异常处理
 * @date 2021-12-24 0:54
 * @verison V1.0.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public R<Void> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return R.failed(e.getErrorCode(), e.getMessage());
        }
        return R.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return R.validateFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public R handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return R.validateFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(WxErrorException.class)
    public R handleWxError(WxErrorException wxError) {
        log.error("call wx service error, ", wxError);
        return R.failed("微信接口服务异常");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error("handleException ", e);
        return R.failed();
    }

    /**
     * RequestParameter 参数缺失异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return R.validateFailed(e.getParameterName() + " not present");
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R handleConstraintViolationException(ConstraintViolationException e) {
        return R.validateFailed(e.getMessage());
    }
}
