package com.sms.eagle.eye.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.context.UserContextHolder;
import com.sms.eagle.eye.backend.model.UserInfo;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserinfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    @Value("${system.security.user-header:UserInfo}")
    public String userinfoHeader = "UserInfo";

    public UserinfoInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userInfoBase64 = request.getHeader(userinfoHeader);
        try {
            UserContextHolder.setUserInfo(objectMapper.readValue(
                Base64.getDecoder().decode(userInfoBase64), UserInfo.class));
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }
}