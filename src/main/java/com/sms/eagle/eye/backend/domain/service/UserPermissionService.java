package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import java.util.Set;

public interface UserPermissionService extends IService<UserPermissionGroupEntity> {

    Set<String> getPermissionByEmail(String email);

    boolean addOrUpdate(UserPermissionGroupRequest request);
}
