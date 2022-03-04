package com.sms.eagle.eye.backend.domain.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import java.util.List;

public interface PermissionGroupService extends IService<PermissionGroupEntity> {

    IPage<PermissionGroupResponse> page(PermissionGroupQueryRequest pageRequest);

    Long saveFromRequest(PermissionGroupRequest request);

    boolean updateFromRequest(PermissionGroupRequest request);

    boolean deleteById(Long id);

    List<PermissionGroupResponse> queryAll();
}
