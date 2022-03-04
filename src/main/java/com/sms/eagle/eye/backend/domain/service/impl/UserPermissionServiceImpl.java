package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionEntity;
import com.sms.eagle.eye.backend.domain.mapper.UserPermissionMapper;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermissionEntity> implements
    UserPermissionService {

    @Override
    public List<String> getPermissionByEmail(String email) {
        return getBaseMapper().getPermissionByEmail(email);
    }

    @Override
    public List<UserPermissionGroupResponse> getAllUserPermissionGroupName() {
        return getBaseMapper().getAllUserPermissionGroupName();
    }

    @Override
    public boolean addOrUpdate(UserPermissionRequest request) {
        LambdaQueryWrapper<UserPermissionEntity> queryWrapper = Wrappers.lambdaQuery(UserPermissionEntity.class)
            .eq(UserPermissionEntity::getEmail, request.getEmail());
        UserPermissionEntity userPermission = getOne(queryWrapper);
        userPermission = Objects
            .requireNonNullElse(userPermission, UserPermissionEntity.builder().email(request.getEmail()).build());
        userPermission.setPermissionGroupId(request.getPermissionGroupId());
        return saveOrUpdate(userPermission);
    }
}
