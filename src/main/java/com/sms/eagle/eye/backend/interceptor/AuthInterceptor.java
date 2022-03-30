package com.sms.eagle.eye.backend.interceptor;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.common.enums.PermissionType;
import com.sms.eagle.eye.backend.domain.service.UserPermissionGroupService;
import com.sms.eagle.eye.backend.exception.ForbiddenException;
import com.sms.eagle.eye.backend.interceptor.order.InterceptorOrder;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor, Ordered {

    private final UserPermissionGroupService userPermissionGroupService;

    public AuthInterceptor(UserPermissionGroupService userPermissionGroupService) {
        this.userPermissionGroupService = userPermissionGroupService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ForbiddenException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object bean = handlerMethod.getBean();
            Set<String> permissions = null;
            PreAuth classPreAuth = AnnotationUtils.findAnnotation(bean.getClass(), PreAuth.class);
            if (classPreAuth != null) {
                permissions = getPermissions();
                verifyPermission(classPreAuth.value(), permissions);
            }
            PreAuth methodPreAuth = handlerMethod.getMethodAnnotation(PreAuth.class);
            if (methodPreAuth != null) {
                permissions = Objects.requireNonNullElse(permissions, getPermissions());
                verifyPermission(methodPreAuth.value(), permissions);
            }
        }
        return true;
    }

    private Set<String> getPermissions() {
        return userPermissionGroupService.getPermissionByEmail(SecurityUtil.getCurrentUser().getEmail());
    }

    private void verifyPermission(PermissionType value, Set<String> permissions) {
        if (!permissions.contains(value.getPermission())) {
            log.warn("Forbidden permission:{}", value.getPermission());
            throw new ForbiddenException();
        }
    }

    @Override
    public int getOrder() {
        return InterceptorOrder.AUTH;
    }
}
