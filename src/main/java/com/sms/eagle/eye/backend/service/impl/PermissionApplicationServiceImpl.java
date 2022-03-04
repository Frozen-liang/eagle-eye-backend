package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.PermissionService;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import com.sms.eagle.eye.backend.service.PermissionApplicationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PermissionApplicationServiceImpl implements PermissionApplicationService {

    private final PermissionService permissionService;

    public PermissionApplicationServiceImpl(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public List<PermissionResponse> list(String name) {
        return permissionService.listByName(name);
    }
}
