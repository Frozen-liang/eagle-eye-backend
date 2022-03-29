package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import com.sms.eagle.eye.backend.service.UserPermissionApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserPermissionApplicationServiceImpl implements UserPermissionApplicationService {

    private final UserPermissionService userPermissionService;

    public UserPermissionApplicationServiceImpl(
        UserPermissionService userPermissionService) {
        this.userPermissionService = userPermissionService;
    }


    @Override
    public boolean addOrUpdate(UserPermissionGroupRequest request) {
        return userPermissionService.addOrUpdate(request);
    }
}
