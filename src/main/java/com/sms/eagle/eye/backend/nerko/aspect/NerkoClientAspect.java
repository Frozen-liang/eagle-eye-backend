package com.sms.eagle.eye.backend.nerko.aspect;

import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class NerkoClientAspect {

    private final NerkoTokenService tokenService;

    public NerkoClientAspect(NerkoTokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 统一处理请求AccessToken失效问题.
     */
    @Around("execution(* com.sms.eagle.eye.backend.nerko.client.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof FeignException || throwable.getCause() instanceof FeignException) {
                FeignException w = throwable instanceof FeignException
                    ? (FeignException) throwable
                    : (FeignException) throwable.getCause();
                if (w.status() == HttpStatus.UNAUTHORIZED.value()) {
                    tokenService.cleanClientCredentialTokenCache();
                    result = joinPoint.proceed();
                } else {
                    throw throwable;
                }
            } else {
                throw throwable;
            }
        }
        return result;
    }
}
