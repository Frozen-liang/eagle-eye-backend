package com.sms.eagle.eye.backend.aspect;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATABASE_OPERATION_FAILURE;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SqlExceptionAspect {

    @Pointcut("@within(com.sms.eagle.eye.backend.aspect.DomainServiceAdvice)")
    public void sqlException() {
    }

    @Around("sqlException()")
    public Object exceptionAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (EagleEyeException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error(DATABASE_OPERATION_FAILURE.getMessage(), exception);
            throw new EagleEyeException(DATABASE_OPERATION_FAILURE);
        }
    }
}