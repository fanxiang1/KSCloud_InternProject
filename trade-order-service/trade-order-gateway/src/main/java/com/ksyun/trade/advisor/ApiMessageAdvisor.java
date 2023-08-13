package com.ksyun.trade.advisor;

import cn.hutool.json.JSONUtil;
import com.ksyun.trade.base.BusinessCodeEnum;
import com.ksyun.trade.base.BusinessException;
import com.ksyun.trade.base.Response;
import com.ksyun.trade.utils.RequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Aspect
@Component
@Slf4j
public class ApiMessageAdvisor {


    @Around("execution(* com.ksyun.trade.controller..*.*(..))")
    public Object invokeAPI(ProceedingJoinPoint pjp) {
        String apiName = this.getApiName(pjp);
        // 生成RequestId
        String requestId = this.getRequestId();
        System.out.println(requestId);
        // 配置日志文件打印 REQUEST_ID
        MDC.put("traceId", requestId);
        Object returnValue = null;
        try{
            // 打印请求参数
            this.printRequestParam(apiName, pjp);
            returnValue = pjp.proceed();
            // 处理RequestId
            this.handleRequestId(returnValue);
        }catch (BusinessException ex){
            // 业务异常
            returnValue = this.handleBusinessException(apiName, ex);
        }catch (Throwable ex){
            // 系统异常
            returnValue = this.handleSystemException(apiName, ex);
        }finally {
            // 打印响应参数
            this.printResponse(apiName, returnValue);
            RequestIdUtils.removeRequestId();
            MDC.clear();
        }
        return returnValue;
    }

    /**
     * 处理系统异常
     * @param apiName 接口名称
     * @param ex 系统异常
     * @return 返回参数
     */
    private Response handleSystemException(String apiName, Throwable ex){
        log.error("@Meet unknown error when do " + apiName + ":" + ex.getMessage(), ex);
        Response response = new Response(BusinessCodeEnum.UNKNOWN_ERROR.getCode(), BusinessCodeEnum.UNKNOWN_ERROR.getMsg());
        response.setRequestId(RequestIdUtils.getRequestId().toString());
        return response;
    }

    /**
     * 处理业务异常
     * @param apiName 接口名称
     * @param ex 业务异常
     * @return 返回参数
     */
    private Response handleBusinessException(String apiName, BusinessException ex){
        log.error("@Meet error when do " + apiName + "[" + ex.getCode() + "]:" + ex.getMsg(), ex);
        Response response = new Response(ex.getCode(), ex.getMsg());
        response.setRequestId(RequestIdUtils.getRequestId().toString());
        return response;
    }

    /**
     * 填充RequestId
     * @param returnValue 返回参数
     */
    private void handleRequestId(Object returnValue){
        if(returnValue instanceof Response){
            Response response = (Response)returnValue;
            response.setRequestId(RequestIdUtils.getRequestId().toString());
        }
    }

    /**
     * 打印响应参数信息
     * @param apiName 接口名称
     * @param returnValue 返回值
     */
    private void printResponse(String apiName, Object returnValue){
        if (log.isInfoEnabled()) {
            log.info("@@{} done, response: {}", apiName, JSONUtil.toJsonStr(returnValue));
        }
    }

    /**
     * 打印请求参数信息
     * @param apiName 接口名称
     * @param pjp 切点
     */
    private void printRequestParam(String apiName, ProceedingJoinPoint pjp){
        Object[] args = pjp.getArgs();
        if(log.isInfoEnabled() && args != null&& args.length > 0){
            for(Object o : args) {
                if(!(o instanceof HttpServletRequest) && !(o instanceof HttpServletResponse) && !(o instanceof CommonsMultipartFile)) {
                    log.info("@@{} started, request: {}", apiName, JSONUtil.toJsonStr(o));
                }
            }
        }
    }

    /**
     * 获取RequestId
     * 优先从header头获取，如果没有则自己生成
     * @return RequestId
     */
    private String getRequestId(){
        // 因为如果有网关，则一般会从网关传递过来，所以优先从header头获取
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null && StringUtils.hasText(attributes.getRequest().getHeader("X-KSY-REQUEST-ID"))) {
            HttpServletRequest request = attributes.getRequest();
            String requestId = request.getHeader("X-KSY-REQUEST-ID");
            //UUID uuid = UUID.fromString(requestId);
            RequestIdUtils.generateRequestId(requestId);
            return requestId;
        }
        String existRequestId = RequestIdUtils.getRequestId();
        if(existRequestId != null){
            return existRequestId;
        }
        RequestIdUtils.generateRequestId();
        return RequestIdUtils.getRequestId();
    }

    /**
     * 获取当前接口对应的类名和方法名
     * @param pjp 切点
     * @return apiName
     */
    private String getApiName(ProceedingJoinPoint pjp){
        String apiClassName = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        return apiClassName.concat(":").concat(methodName);
    }
}