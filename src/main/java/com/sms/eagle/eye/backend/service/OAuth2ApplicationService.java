package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import com.sms.eagle.eye.backend.response.user.UserResponse;
import java.util.List;

public interface OAuth2ApplicationService {

    String getAccessToken(String code);

    UserResponse getUserInfo();

    List<UserPermissionGroupResponse> getUsers();
}