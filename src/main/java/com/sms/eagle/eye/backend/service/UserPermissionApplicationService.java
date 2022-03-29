package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;

public interface UserPermissionApplicationService {

    boolean addOrUpdate(UserPermissionGroupRequest request);
}
