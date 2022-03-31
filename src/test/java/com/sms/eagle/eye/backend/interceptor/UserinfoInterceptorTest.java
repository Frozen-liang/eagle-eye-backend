package com.sms.eagle.eye.backend.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.context.UserContextHolder;
import com.sms.eagle.eye.backend.interceptor.order.InterceptorOrder;
import com.sms.eagle.eye.backend.model.UserInfo;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class UserinfoInterceptorTest {

    String header = "UserInfo";
    String userInfoBase64 = "user";
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Object handler = mock(Object.class);
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    UserinfoInterceptor userinfoInterceptor = spy(new UserinfoInterceptor(objectMapper));

    private static final MockedStatic<UserContextHolder> USER_CONTEXT_HOLDER_MOCKED_STATIC;

    static {
        USER_CONTEXT_HOLDER_MOCKED_STATIC = Mockito.mockStatic(UserContextHolder.class);
    }

    @AfterAll
    public static void close() {
        USER_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    @Test
    void preHandle_test() throws IOException {
        when(request.getHeader(header)).thenReturn(userInfoBase64);
        UserInfo userInfo = mock(UserInfo.class);
        when(objectMapper.readValue(Base64.getDecoder().decode(userInfoBase64), UserInfo.class)).thenReturn(userInfo);
        assertThat(userinfoInterceptor.preHandle(request, response, handler)).isTrue();
    }

    @Test
    void preHandle_exception_test() throws IOException {
        when(request.getHeader(header)).thenReturn(userInfoBase64);
        when(objectMapper.readValue(Base64.getDecoder().decode(userInfoBase64), UserInfo.class)).thenThrow(new RuntimeException());
        assertThat(userinfoInterceptor.preHandle(request, response, handler)).isTrue();
    }

    @Test
    void getOrder_test() {
        int order = userinfoInterceptor.getOrder();
        assertThat(order).isEqualTo(InterceptorOrder.USER_INFO);
    }
}