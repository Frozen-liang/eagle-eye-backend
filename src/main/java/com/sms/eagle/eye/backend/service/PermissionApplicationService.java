package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.List;

public interface PermissionApplicationService {

    List<PermissionResponse> list(String name);

}
