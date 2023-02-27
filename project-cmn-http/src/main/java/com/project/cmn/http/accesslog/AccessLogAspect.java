package com.project.cmn.http.accesslog;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

/**
 * URL 호출 시 거치는 메소드에 대한 정보를 남기기 위한 AOP
 */
@Aspect
public class AccessLogAspect {

    @Around(value = "execution(* com.project..*Controller.*(..)) || execution(* com.project..*Service.*(..)) || execution(* com.project..*ServiceImpl.*(..)) || execution(* com.project..*Mapper.*(..))")
    public Object arroundLogging(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String executeMethodName = signature.getMethod().getName();

        StopWatch stopWatch = AccessLog.getAccessLogDto().getStopWatch();

        if (stopWatch != null) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            stopWatch.start(String.format("%s.%s", signature.getDeclaringType().getSimpleName(), executeMethodName));
        }

        Object result = pjp.proceed();

        if (stopWatch != null && stopWatch.isRunning()) {
            stopWatch.stop();
        }

        return result;
    }
}
