package com.sms.eagle.eye.backend.interceptor;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.exception.ForbiddenException;
import com.sms.eagle.eye.backend.interceptor.order.InterceptorOrder;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    private final UserPermissionService userPermissionService;

    public AuthInterceptor(UserPermissionService userPermissionService) {
        this.userPermissionService = userPermissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws ForbiddenException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object bean = handlerMethod.getBean();
            List<String> permissions = null;
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

    private List<String> getPermissions() {
        return userPermissionService.getPermissionByEmail(SecurityUtil.getCurrentUser().getEmail());
    }

    private void verifyPermission(String[] value, List<String> permissions) {
        if (!Arrays.stream(value).allMatch(permissions::contains)) {
            log.error("Forbidden permission:{}", Arrays.toString(value));
            throw new ForbiddenException();
        }
    }

    @Override
    public int getOrder() {
        return InterceptorOrder.AUTH;
    }
}
