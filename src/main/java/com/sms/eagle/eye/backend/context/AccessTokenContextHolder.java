package com.sms.eagle.eye.backend.context;

import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;

public class AccessTokenContextHolder {

    private static NerkoTokenService TOKEN_SERVICE;

    public static void setTokenService(NerkoTokenService tokenService) {
        TOKEN_SERVICE = tokenService;
    }

    public static NerkoTokenService getTokenService() {
        return TOKEN_SERVICE;
    }
}