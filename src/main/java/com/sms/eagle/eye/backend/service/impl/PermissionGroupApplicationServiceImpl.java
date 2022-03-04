package com.sms.eagle.eye.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupConnEntity;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupConnService;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupConnRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.service.PermissionGroupApplicationService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionGroupApplicationServiceImpl implements PermissionGroupApplicationService {

    private final PermissionGroupService permissionGroupService;
    private final PermissionGroupConnService permissionGroupConnService;


    public PermissionGroupApplicationServiceImpl(PermissionGroupService permissionGroupService,
        PermissionGroupConnService permissionGroupConnService) {
        this.permissionGroupService = permissionGroupService;
        this.permissionGroupConnService = permissionGroupConnService;
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
        LambdaQueryWrapper<PermissionGroupConnEntity> queryWrapper = Wrappers
            .lambdaQuery(PermissionGroupConnEntity.class)
            .eq(PermissionGroupConnEntity::getGroupId, id);
        permissionGroupConnService.remove(queryWrapper);
        return permissionGroupService.deleteById(id);
    }

    @Override
    public boolean addPermission(PermissionGroupConnRequest request) {
        LambdaQueryWrapper<PermissionGroupConnEntity> queryWrapper =
            getPermissionGroupConnEntityLambdaQueryWrapper(request);
        if (permissionGroupConnService.count(queryWrapper) > 0) {
            return false;
        }
        PermissionGroupConnEntity groupConnEntity = PermissionGroupConnEntity.builder()
            .groupId(request.getGroupId())
            .permissionId(request.getPermissionId())
            .build();
        return permissionGroupConnService.save(groupConnEntity);
    }

    @Override
    public boolean removePermission(PermissionGroupConnRequest request) {
        LambdaQueryWrapper<PermissionGroupConnEntity> queryWrapper =
            getPermissionGroupConnEntityLambdaQueryWrapper(request);
        return permissionGroupConnService.remove(queryWrapper);
    }

    @Override
    public List<PermissionGroupResponse> list() {
        return permissionGroupService.queryAll();
    }

    private LambdaQueryWrapper<PermissionGroupConnEntity> getPermissionGroupConnEntityLambdaQueryWrapper(
        PermissionGroupConnRequest request) {
        return Wrappers
            .lambdaQuery(PermissionGroupConnEntity.class)
            .eq(PermissionGroupConnEntity::getGroupId, request.getGroupId())
            .eq(PermissionGroupConnEntity::getPermissionId, request.getPermissionId());
    }


}
