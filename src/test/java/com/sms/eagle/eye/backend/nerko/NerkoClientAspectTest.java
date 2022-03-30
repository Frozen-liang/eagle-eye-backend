package com.sms.eagle.eye.backend.nerko;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.nerko.aspect.NerkoClientAspect;
import com.sms.eagle.eye.backend.nerko.service.impl.NerkoCoordinationServiceImpl;
import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import com.sms.eagle.eye.backend.service.impl.NotificationChannelApplicationServiceImpl;
import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NerkoClientAspectTest {

    private final NerkoTokenService tokenService = mock(NerkoTokenService.class);
    private final NerkoClientAspect aspect = spy(new NerkoClientAspect(tokenService));

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 根据参数 ProceedingJoinPoint 统一处理请求AccessToken失效问题.
     * <p>
     * 情况1：正常赋值
     */
    @Test
    public void around_test1() throws Throwable {
        // 构建请求对象
        Object OBJECT = Object.class;
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenReturn(OBJECT);
        // 执行
        Object result = aspect.around(joinPoint);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(OBJECT);
    }

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 根据参数 ProceedingJoinPoint 统一处理请求AccessToken失效问题.
     * <p>
     * 情况2：joinPoint.proceed()抛出Throwable异常
     */
    @Test
    public void around_exception_test1() throws Throwable {
        // 构建请求对象
        Object OBJECT = Object.class;
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(new Throwable());
        // 验证
        assertThatThrownBy(() -> aspect.around(joinPoint)).isInstanceOf(Throwable.class);
    }

    /**
     * {@link NerkoClientAspect#around(ProceedingJoinPoint)}.
     * <p>
     * 根据参数 ProceedingJoinPoint 统一处理请求AccessToken失效问题.
     * <p>
     * 情况3：FeignException 抛出Throwable异常
     */
    @Test
    public void around_exception_test2() throws Throwable {
        // 构建请求对象
        Object OBJECT = Object.class;
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Throwable throwable = new Throwable();
        when(joinPoint.proceed()).thenThrow(throwable);
        FeignException feignException = mock(FeignException.class);
        throwable = feignException;
        when(throwable.getCause()).thenReturn(feignException);
        // 验证
        assertThatThrownBy(() -> aspect.around(joinPoint)).isInstanceOf(Throwable.class);
    }
}