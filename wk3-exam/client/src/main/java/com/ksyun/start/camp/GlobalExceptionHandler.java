package com.ksyun.start.camp;

import com.ksyun.start.camp.dto.ResponseDTO;
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
    public ResponseDTO handeException(final Exception ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError("接口出错了，请稍后重试");
        responseDTO.setResult(null);
        return responseDTO;
    }
}
