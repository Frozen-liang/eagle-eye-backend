package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;

public interface UserPermissionGroupApplicationService {

    boolean addOrUpdate(UserPermissionGroupRequest request);
}
