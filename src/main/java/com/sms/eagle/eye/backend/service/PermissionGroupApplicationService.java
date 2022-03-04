package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupConnRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import java.util.List;

public interface PermissionGroupApplicationService {

    CustomPage<PermissionGroupResponse> page(PermissionGroupQueryRequest pageRequest);

    String save(PermissionGroupRequest request);

    boolean update(PermissionGroupRequest request);

    boolean delete(Long id);

    boolean addPermission(PermissionGroupConnRequest request);

    boolean removePermission(PermissionGroupConnRequest request);

    List<PermissionGroupResponse> list();
}
