package com.missyang.usercenter1.common;

public enum ErrorCode {

    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "要查询的值为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "没有权限", ""),
    SUCCESS(0, "成功", ""),
    SYSTEM_ERROR(50000, "内部异常", "");

    private final int code;

    private final String message;

    private final String description;

    // 这个函数写着是为了给最上面的枚举用的，不是给别的文件用的
    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
