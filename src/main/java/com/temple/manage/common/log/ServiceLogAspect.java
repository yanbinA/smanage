package com.temple.manage.common.log;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 * 服务日志切面处理
 * </p>
 *
 * @author messi
 * @package com.temple.manage.common.log
 * @description 服务日志切面处理
 * @date 2022-01-01 16:59
 * @verison V1.0.0
 */
@Aspect
@Component
@Order(2)
@Slf4j
public class ServiceLogAspect {
    @Pointcut("execution(public * com.temple.manage.service.*.*(..))||execution(public * com.temple.manage.service.*.*(..))")
    public void serviceLog() {
    }

    @Around("serviceLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        //记录请求信息
        ServiceLog serviceLog = new ServiceLog();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        serviceLog.setClassName(joinPoint.getTarget().getClass().getName());
        serviceLog.setMethodName(method.getName());
        serviceLog.setParameter(joinPoint.getArgs());
        serviceLog.setResult(result);
        serviceLog.setSpendTime((int) (endTime - startTime));
        serviceLog.setStartTime(startTime);
        log.info(JSONUtil.parse(serviceLog).toString());
        return result;
    }
}
