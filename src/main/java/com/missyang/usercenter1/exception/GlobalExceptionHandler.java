package com.missyang.usercenter1.exception;

import com.missyang.usercenter1.common.BaseResponse;
import com.missyang.usercenter1.common.ErrorCode;
import com.missyang.usercenter1.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Spring AOP 在调用该方法前后做一个额外的处理
@Slf4j  // 写 log 日志
public class GlobalExceptionHandler {

    @ExceptionHandler
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runTimeExceptionHandler(RuntimeException r) {

        log.error("runTimeException" ,r);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, r.getMessage(), "");
    }
}
