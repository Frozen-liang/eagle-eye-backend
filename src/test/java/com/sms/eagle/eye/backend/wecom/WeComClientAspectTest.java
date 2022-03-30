package com.sms.eagle.eye.backend.wecom;

import com.sms.eagle.eye.backend.wecom.aspect.WeComClientAspect;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeComClientAspectTest {

    private final WeComManager weComManager = mock(WeComManager.class);
    private final WeComClientAspect weComClientAspect = new WeComClientAspect(weComManager);

    /**
     * {@link TokenService#getAccessToken(String)}.
     *
     * 处理请求AccessToken失效问题.
     *
     * 情况1：无异常
     */
    @Test
    public void around_test1() throws Throwable {
        // 请求构建参数
        Object OBJ = Object.class;
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenReturn(OBJ);
        // 执行
        Object result = weComClientAspect.around(joinPoint);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(OBJ);
    }

    /**
     * {@link TokenService#getAccessToken(String)}.
     *
     * 处理请求AccessToken失效问题.
     *
     * 情况2：当异常不为WeComException时
     */
    @Test
    public void around_test2() throws Throwable {
        // 请求构建参数
        Object OBJ = Object.class;
        // mock
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(Throwable.class);
        // 验证 异常
        assertThatThrownBy(()->weComClientAspect.around(joinPoint))
                .isInstanceOf(Throwable.class);
    }

    /**
     * {@link TokenService#getAccessToken(String)}.
     *
     * 处理请求AccessToken失效问题.
     *
     * 情况3：当异常是WeComException时
     */
    @Test
    public void around_test3() throws Throwable {
//        // 请求构建参数
//        Object OBJ = Object.class;
//        // mock
//        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
//        when(joinPoint.proceed()).thenThrow(Throwable.class);
//        // 验证 异常
//        assertThatThrownBy(()->weComClientAspect.around(joinPoint))
//                .isInstanceOf(Throwable.class);
    }
}
