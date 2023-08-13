package com.ksyun.trade.interceptor;

import com.ksyun.trade.annotation.RateLimit;
import com.ksyun.trade.base.BusinessException;
import com.ksyun.trade.utils.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Slf4j
//@Aspect
//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    private ExpressionParser parser = new SpelExpressionParser();

    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    // 预处理回调方法，在接口调用之前使用  true代表放行  false代表不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RateLimit limit = method.getAnnotation(RateLimit.class);
        if (limit != null) {
            String key = limit.key();
            key = method.getName() + "#" + key;
            if (this.rateLimiter.limit(limit.limitCount(), limit.seconds(), key)) {
                return true;
            } else {
                throw new BusinessException("429","对不起, 系统压力过大, 请稍后再试!");
            }
        } else {
            return true;
        }
    }

    // 接口调用之后，返回之前 使用
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    // 整个请求完成后，在视图渲染前使用
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

}


//    @Around("@annotation(com.ksyun.trade.annotation.RateLimit)")
//    public Object before(ProceedingJoinPoint pjp) throws Throwable,BusinessException {
//            MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
//            Method method = joinPointObject.getMethod();
//            RateLimit limit = method.getAnnotation(RateLimit.class);
//            if(null == limit) {
//                return pjp.proceed();
//            }
//            Object[] args = pjp.getArgs();
//            String[] params = discoverer.getParameterNames(method);
//            EvaluationContext context = new StandardEvaluationContext();
//            for (int i = 0; i < params.length; i++) {
//                context.setVariable(params[i],args[i]);
//            }
////        String keySpel = limit.key();
//            String key = limit.key();
////        Expression keyExpression = parser.parseExpression(keySpel);
////        String key = keyExpression.getValue(context,String.class);
//            key = method.getName() + "#" + key;
//            boolean accessable = true;
////            try {
//                accessable = this.rateLimiter.limit(limit.limitCount(), limit.seconds(),key);
////            } catch (Exception e) {
////                log.error("RateLimiterInterceptor.before check limit occur error;key={}",key,e);
////                accessable = false;
////            }
//            // 对不起, 系统压力过大, 请稍后再试,抛出异常
//            if(!accessable) {
//                log.error("对不起, 系统压力过大, 请稍后再试!");
//                throw new RuntimeException();
//            }
//            return pjp.proceed();
//    }
//}