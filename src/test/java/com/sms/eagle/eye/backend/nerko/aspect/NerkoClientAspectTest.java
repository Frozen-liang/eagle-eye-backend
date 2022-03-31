package com.sms.eagle.eye.backend.nerko.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class NerkoClientAspectTest {

    private final NerkoTokenService tokenService = mock(NerkoTokenService.class);
    private final NerkoClientAspect aspect = spy(new NerkoClientAspect(tokenService));

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
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
        Object result = aspect.around(joinPoint);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(object);
    }

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况2：当异常不为WeComException时
     */
    @Test
    public void around_test2() throws Throwable {
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(RuntimeException.class);
        // invoke and assert
        Assertions.assertThatThrownBy(() -> aspect.around(joinPoint))
            .isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况3：当异常是 FeignException 但 status 不是 {@link HttpStatus#UNAUTHORIZED}
     */
    @Test
    public void around_test3() throws Throwable {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.proceed()
        when(joinPoint.proceed()).thenThrow(FeignException.class);
        // invoke and assert
        Assertions.assertThatThrownBy(() -> aspect.around(joinPoint))
            .isInstanceOf(FeignException.class);
    }

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 情况3：当异常是 FeignException 且 status 是 {@link HttpStatus#UNAUTHORIZED}
     */
    @Test
    public void around_test4() throws Throwable {
        // request
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        // mock joinPoint.proceed()
        Object object = "123";
        FeignException feignException = mock(FeignException.class);
        when(joinPoint.proceed()).thenThrow(feignException).thenReturn(object);
        // mock feignException.getErrorCode()
        when(feignException.status()).thenReturn(HttpStatus.UNAUTHORIZED.value());
        // mock clearAccessToken()
        doNothing().when(tokenService).cleanClientCredentialTokenCache();
        // invoke and assert
        Object result = aspect.around(joinPoint);
        // assert
        assertThat(result).isEqualTo(object);
    }
}