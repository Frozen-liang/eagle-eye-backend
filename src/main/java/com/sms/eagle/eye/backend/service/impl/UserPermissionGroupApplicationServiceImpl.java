package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.UserPermissionGroupService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import com.sms.eagle.eye.backend.service.UserPermissionGroupApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserPermissionGroupApplicationServiceImpl implements UserPermissionGroupApplicationService {

    private final UserPermissionGroupService userPermissionGroupService;

    public UserPermissionGroupApplicationServiceImpl(
        UserPermissionGroupService userPermissionGroupService) {
        this.userPermissionGroupService = userPermissionGroupService;
    }


    @Override
    public boolean addOrUpdate(UserPermissionGroupRequest request) {
        return userPermissionGroupService.addOrUpdate(request);
    }
}
