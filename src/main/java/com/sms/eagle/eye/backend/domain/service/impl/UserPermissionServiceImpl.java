package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.UserPermissionMapper;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import io.vavr.control.Try;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermissionGroupEntity> implements
    UserPermissionService {

    private final ObjectMapper objectMapper;

    public UserPermissionServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<String> getPermissionByEmail(String email) {
        String permission = getBaseMapper().getPermissionByEmail(email);
        return Try.of(() -> objectMapper.readValue(permission, new TypeReference<Set<String>>() {
        })).getOrElse(new HashSet<>());
    }

    @Override
    public boolean addOrUpdate(UserPermissionGroupRequest request) {
        LambdaQueryWrapper<UserPermissionGroupEntity> queryWrapper = Wrappers
            .lambdaQuery(UserPermissionGroupEntity.class)
            .eq(UserPermissionGroupEntity::getEmail, request.getEmail());
        UserPermissionGroupEntity userPermission = getOne(queryWrapper);
        userPermission = Objects
            .requireNonNullElse(userPermission, UserPermissionGroupEntity.builder().email(request.getEmail()).build());
        userPermission.setPermissionGroupId(request.getPermissionGroupId());
        return saveOrUpdate(userPermission);
    }
}
