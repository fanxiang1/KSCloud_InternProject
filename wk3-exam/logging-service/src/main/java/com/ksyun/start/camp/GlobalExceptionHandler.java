package com.ksyun.start.camp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的异常处理.
 *
 * @author fanxiang1
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse handeException(final Exception ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(500);
        apiResponse.setData("服务器内部错误，请稍后重试");
        return apiResponse;
    }
}
