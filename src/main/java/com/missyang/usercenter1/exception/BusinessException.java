package com.missyang.usercenter1.exception;

import com.missyang.usercenter1.common.ErrorCode;

public class BusinessException extends RuntimeException{

    private final int code; // 因为不需要 setter，所以直接给 final 枚举

    private final String description;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
