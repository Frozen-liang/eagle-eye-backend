package com.sms.eagle.eye.backend.aspect;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATABASE_OPERATION_FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

public class SqlExceptionAspectTest {

    SqlExceptionAspect sqlExceptionAspect = spy(new SqlExceptionAspect());

    @Test
    void exceptionAround_test() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Object object = new Object();
        doReturn(object).when(joinPoint).proceed();
        assertThat(sqlExceptionAspect.exceptionAround(joinPoint)).isEqualTo(object);
    }

    @Test
    void exceptionAround_test2() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(new EagleEyeException(DATABASE_OPERATION_FAILURE));
        assertThatThrownBy(() -> sqlExceptionAspect.exceptionAround(joinPoint))
            .isInstanceOf(EagleEyeException.class);
    }

    @Test
    void exceptionAround_test3() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sqlExceptionAspect.exceptionAround(joinPoint))
            .isInstanceOf(EagleEyeException.class);
    }
}