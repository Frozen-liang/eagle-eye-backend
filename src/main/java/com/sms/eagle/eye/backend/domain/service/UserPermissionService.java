package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionEntity;
import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import java.util.List;

public interface UserPermissionService extends IService<UserPermissionEntity> {

    List<String> getPermissionByEmail(String email);

    List<UserPermissionGroupResponse> getAllUserPermissionGroupName();

    boolean addOrUpdate(UserPermissionRequest request);
}
