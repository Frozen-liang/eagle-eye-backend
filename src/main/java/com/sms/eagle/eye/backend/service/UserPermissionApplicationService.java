package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;

public interface UserPermissionApplicationService {

    boolean addOrUpdate(UserPermissionRequest request);
}
