package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.response.UserResponse;

public interface OAuth2ApplicationService {

    String getAccessToken(String code);

    UserResponse getUserInfo();
}