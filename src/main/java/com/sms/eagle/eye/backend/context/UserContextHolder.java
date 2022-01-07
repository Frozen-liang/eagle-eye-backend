package com.sms.eagle.eye.backend.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.sms.eagle.eye.backend.model.UserInfo;

public abstract class UserContextHolder {

    private static final TransmittableThreadLocal<UserInfo> userInfoContext = new TransmittableThreadLocal<>();

    public static void restUserInfo() {
        userInfoContext.remove();
    }

    public static UserInfo getUserInfo() {
        return userInfoContext.get();
    }

    public static void setUserInfo(UserInfo userInfo) {
        userInfoContext.set(userInfo);
    }
}