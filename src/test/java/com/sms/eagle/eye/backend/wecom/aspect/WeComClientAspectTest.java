package com.sms.eagle.eye.backend.wecom.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;
import com.sms.eagle.eye.backend.wecom.exception.WeComException;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class WeComClientAspectTest {

    private final WeComManager weComManager = mock(WeComManager.class);
    private final WeComClientAspect weComClientAspect = spy(new WeComClientAspect(weComManager));

    /**
     * {@link WeComClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 处理请求AccessToken失效问题.
     * <p>
     * 情况1：无异常
     */
    @Test
    public void around_test1() throws Throwable {
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // 请求构建参数
        Object object = "123";
        when(joinPoint.proceed()).thenReturn(object);
        // 执行
        Object result = weComClientAspect.around(joinPoint);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(object);
    }

    /**
     * {@link WeComClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况2：当异常不为WeComException时
     */
    @Test
    public void around_test2() throws Throwable {
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(RuntimeException.class);
        // invoke and assert
        assertThatThrownBy(() -> weComClientAspect.around(joinPoint))
            .isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link WeComClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况3：当异常是 WeComException 但 errorCode 为 null 时
     */
    @Test
    public void around_test3() throws Throwable {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.proceed()
        WeComException weComException = new WeComException("");
        when(joinPoint.proceed()).thenThrow(weComException);
        // invoke and assert
        assertThatThrownBy(() -> weComClientAspect.around(joinPoint))
            .isInstanceOf(WeComException.class);
    }

    /**
     * {@link WeComClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况3：当异常是 WeComException 且 errorCode 不为 null
     */
    @Test
    public void around_test4() throws Throwable {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.proceed()
        Object object = "123";
        WeComException weComException = mock(WeComException.class);
        when(joinPoint.proceed()).thenThrow(weComException).thenReturn(object);
        // mock weComException.getErrorCode()
        WeComErrorCode weComErrorCode = mock(WeComErrorCode.class);
        when(weComException.getErrorCode()).thenReturn(weComErrorCode);
        // mock weComErrorCode.getErrorCode()
        String errorCode = "errorCode";
        when(weComErrorCode.getErrorCode()).thenReturn(errorCode);
        // mock weComClientAspect.needRetry()
        when(weComClientAspect.needRetry(errorCode)).thenReturn(true);
        // mock weComClientAspect.getApplicationName
        String applicationName = "applicationName";
        doReturn(applicationName).when(weComClientAspect).getApplicationName(joinPoint);
        // weComManager.tokenService()
        TokenService tokenService = mock(TokenService.class);
        when(weComManager.tokenService()).thenReturn(tokenService);
        // mock clearAccessToken()
        doNothing().when(tokenService).clearAccessToken(applicationName);
        // invoke and assert
        Object result = weComClientAspect.around(joinPoint);
        // assert
        assertThat(result).isEqualTo(object);
    }

    /**
     * {@link WeComClientAspect#getApplicationName(ProceedingJoinPoint)}.
     * TODO can not mock PostMapping
     */
    @Disabled
    @Test
    void getApplicationName_test_1() {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.getSignature()
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        // mock methodSignature.getMethod()
        Method method = mock(Method.class);
        when(methodSignature.getMethod()).thenReturn(method);
        // mock method.getAnnotation(PostMapping.class)
        PostMapping postMapping = mock(PostMapping.class);
        when(method.getAnnotation(PostMapping.class)).thenReturn(postMapping);
        // mock postMapping.headers()
        String[] headers = new String[]{"123", "123"};
        doReturn(headers).when(postMapping).headers();
        // mock joinPoint.getArgs()
        Object[] objects = new Object[]{"123", "567"};
        doReturn(objects).when(joinPoint).getArgs();
        // invoke
        String result = weComClientAspect.getApplicationName(joinPoint);
        // assert
        assertThat(result).isEqualTo("567");
    }

    /**
     * {@link WeComClientAspect#getApplicationName(ProceedingJoinPoint)}.
     * TODO can not mock PostMapping or GetMapping
     */
    @Disabled
    @Test
    void getApplicationName_test_2() {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.getSignature()
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        // mock methodSignature.getMethod()
        Method method = mock(Method.class);
        when(methodSignature.getMethod()).thenReturn(method);
        // mock method.getAnnotation(PostMapping.class)
        when(method.getAnnotation(PostMapping.class)).thenReturn(null);
        // mock method.getAnnotation
        GetMapping getMapping = mock(GetMapping.class);
        when(method.getAnnotation(GetMapping.class)).thenReturn(getMapping);
        // mock postMapping.headers()
        String[] headers = new String[]{"123", "123"};
        doReturn(headers).when(getMapping).headers();
        // mock joinPoint.getArgs()
        Object[] objects = new Object[]{"123", "567"};
        doReturn(objects).when(joinPoint).getArgs();
        // invoke
        String result = weComClientAspect.getApplicationName(joinPoint);
        // assert
        assertThat(result).isEqualTo("567");
    }
}
