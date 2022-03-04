package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionEntity;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.List;

public interface PermissionService extends IService<PermissionEntity> {

    List<PermissionResponse> listByName(String name);
}
