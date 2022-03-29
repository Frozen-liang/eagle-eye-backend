package com.sms.eagle.eye.backend.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.common.enums.PermissionType;
import com.sms.eagle.eye.backend.context.UserContextHolder;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

public class AuthInterceptorTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HandlerMethod handler = mock(HandlerMethod.class);
    UserPermissionService userPermissionService = mock(UserPermissionService.class);
    AuthInterceptor authInterceptor = spy(new AuthInterceptor(userPermissionService));

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;
    private static final MockedStatic<AnnotationUtils> ANNOTATION_UTILS_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
        ANNOTATION_UTILS_MOCKED_STATIC = mockStatic(AnnotationUtils.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser)
            .thenReturn(UserInfo.builder().email("email@test.com").build());
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
        ANNOTATION_UTILS_MOCKED_STATIC.close();
    }

    @Test
    void preHandle_test() {
        Object obj = new Object();
        when(handler.getBean()).thenReturn(obj);
        when(userPermissionService.getPermissionByEmail(any()))
            .thenReturn(Set.of(PermissionType.PERMISSION_GROUP_EDIT.getPermission()));
        PreAuth preAuth = mock(PreAuth.class);
        ANNOTATION_UTILS_MOCKED_STATIC.when(() -> AnnotationUtils.findAnnotation(obj.getClass(), PreAuth.class))
            .thenReturn(preAuth);
        when(handler.hasMethodAnnotation(PreAuth.class)).thenReturn(true);
        doReturn(PermissionType.PERMISSION_GROUP_EDIT).when(preAuth).value();
        doReturn(preAuth).when(handler).getMethodAnnotation(PreAuth.class);
        assertThat(authInterceptor.preHandle(request, response, handler)).isTrue();
    }


}