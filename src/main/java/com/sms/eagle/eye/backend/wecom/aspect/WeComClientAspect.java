package com.sms.eagle.eye.backend.wecom.aspect;

import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;
import com.sms.eagle.eye.backend.wecom.exception.WeComException;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 统一处理微信授权码无效问题.
 */
@Slf4j
@Aspect
@Component
public class WeComClientAspect {

    private final WeComManager weComManager;

    public WeComClientAspect(WeComManager weComManager) {
        this.weComManager = weComManager;
    }

    /**
     * 统一处理请求AccessToken失效问题.
     */
    @Around("execution(* com.sms.eagle.eye.backend.wecom.client.WeComClient.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof WeComException || throwable.getCause() instanceof WeComException) {
                WeComException w = throwable instanceof WeComException
                    ? (WeComException) throwable
                    : (WeComException) throwable.getCause();
                if (Objects.nonNull(w.getErrorCode()) && needRetry(w.getErrorCode().getErrorCode())) {
                    String applicationName = getApplicationName(joinPoint);
                    if (StringUtils.isNotEmpty(applicationName)) {
                        weComManager.tokenService().clearAccessToken(applicationName);
                    }
                    // 重试.
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

    /**
     * 获取请求的应用.
     */
    protected String getApplicationName(ProceedingJoinPoint joinPoint) {
        String applicationName = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            String[] headers = postMapping.headers();
            if (headers.length > 0) {
                // 获取应用
                Object[] objects = joinPoint.getArgs();
                if (objects.length > 0) {
                    // 应用这个参数放在最后
                    applicationName = (String) objects[objects.length - 1];
                }
            }
        } else {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                String[] headers = getMapping.headers();
                if (headers.length > 0) {
                    // 获取应用
                    Object[] objects = joinPoint.getArgs();
                    if (objects.length > 0) {
                        // 应用这个参数放在最后
                        applicationName = (String) objects[objects.length - 1];
                    }
                }
            }
        }
        return applicationName;
    }

    /**
     * 如果微信服务调用返回码为
     * 40014[不合法的access_token]
     * 41001[缺少access_token参数]
     * 42001[access_token已过期]
     * 则重新获取授权码，重试一次.
     */
    protected boolean needRetry(String errorCode) {
        boolean needRetry = false;
        if (WeComErrorCode.ERROR_CODE_40014.getErrorCode().equals(errorCode)) {
            needRetry = true;
        }
        if (WeComErrorCode.ERROR_CODE_41001.getErrorCode().equals(errorCode)) {
            needRetry = true;
        }
        if (WeComErrorCode.ERROR_CODE_42001.getErrorCode().equals(errorCode)) {
            needRetry = true;
        }
        return needRetry;
    }
}
