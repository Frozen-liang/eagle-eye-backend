package com.sms.eagle.eye.backend.utils;

import com.sms.eagle.eye.backend.context.UserContextHolder;
import com.sms.eagle.eye.backend.exception.UnauthorizedException;
import com.sms.eagle.eye.backend.model.UserInfo;
import java.util.Objects;

public class SecurityUtil {

    private static void validUser(UserInfo userInfo) {
        if (Objects.isNull(userInfo) || Objects.isNull(userInfo.getUsername())) {
            throw new UnauthorizedException();
        }
    }

    public static UserInfo getCurrentUser() {
        UserInfo userInfo = UserContextHolder.getUserInfo();
        validUser(userInfo);
        return userInfo;
    }

}