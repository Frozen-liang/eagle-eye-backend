package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.PermissionGroupService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.AddOrRemovePermissionRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.service.PermissionGroupApplicationService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionGroupApplicationServiceImpl implements PermissionGroupApplicationService {

    private final PermissionGroupService permissionGroupService;


    public PermissionGroupApplicationServiceImpl(PermissionGroupService permissionGroupService) {
        this.permissionGroupService = permissionGroupService;
    }

    @Override
    public CustomPage<PermissionGroupResponse> page(PermissionGroupQueryRequest pageRequest) {
        return new CustomPage<>(permissionGroupService.page(pageRequest));
    }

    @Override
    public String save(PermissionGroupRequest request) {
        return permissionGroupService.saveFromRequest(request).toString();
    }

    @Override
    public boolean update(PermissionGroupRequest request) {
        return permissionGroupService.updateFromRequest(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return permissionGroupService.deleteById(id);
    }

    @Override
    public boolean addPermission(AddOrRemovePermissionRequest request) {
        Optional.ofNullable(permissionGroupService.getOne(request.getGroupId()))
            .ifPresent(permissionGroup -> {
                    permissionGroup.getPermissions().add(request.getPermission());
                    permissionGroupService.updateById(permissionGroup);
                }
            );
        return true;
    }

    @Override
    public boolean removePermission(AddOrRemovePermissionRequest request) {
        Optional.ofNullable(permissionGroupService.getOne(request.getGroupId()))
            .ifPresent(permissionGroup -> {
                    permissionGroup.getPermissions().remove(request.getPermission());
                    permissionGroupService.updateById(permissionGroup);
                }
            );
        return true;
    }

    @Override
    public List<PermissionGroupResponse> list() {
        return permissionGroupService.queryAll();
    }
}
