package com.temple.manage.common.api;

/**
 * <p>
 * API返回码
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.api
 * @description API返回码
 * @date 2021-12-24 0:56
 * @verison V1.0.0
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    FILE_NOT_EXIST(405, "上传文件不存在"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    USER_NOT_EXIST(406, "用户信息失效,请重新登陆"),
    SUBMIT_INCOMPLETE(410, "未完成检测内容"),
    SUBMIT_COMPLETE(411, "已提交,请勿重复操作"),
    NO_SUBMIT_COMPLETE(412, "审核员未提交,请勿操作"),
    NO_DATA(416, "数据不存在");
    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
