package com.ksyun.trade.config;

import com.ksyun.trade.base.BusinessCodeEnum;
import com.ksyun.trade.base.BusinessException;
import com.ksyun.trade.dto.ErrorDTO;
import com.ksyun.trade.dto.UpstreamDTO;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class WebExceptionControl {

    @Autowired
    HttpServletResponse response;
    @ExceptionHandler(BusinessException.class)
    public ErrorDTO BusinessExceptionHandler(BusinessException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(e.getCode());
        // 获得传过来的requestId
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String traceId=request.getHeader("X-KSY-REQUEST-ID");
        errorDTO.setRequestId(traceId);
        errorDTO.setMsg(e.getMsg());
        response.setStatus(429);
        return errorDTO;
    }
}