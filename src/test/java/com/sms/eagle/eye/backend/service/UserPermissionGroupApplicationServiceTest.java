package com.sms.eagle.eye.backend.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.service.UserPermissionGroupService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import com.sms.eagle.eye.backend.service.impl.UserPermissionGroupApplicationServiceImpl;
import org.junit.jupiter.api.Test;

public class UserPermissionGroupApplicationServiceTest {

    private final UserPermissionGroupService userPermissionGroupService = mock(UserPermissionGroupService.class);
    private final UserPermissionGroupApplicationService userPermissionGroupApplicationService =
        new UserPermissionGroupApplicationServiceImpl(userPermissionGroupService);

    @Test
    void addOrUpdate_test() {
        // 构建请求参数
        UserPermissionGroupRequest request = UserPermissionGroupRequest.builder()
            .email("email@test.com")
            .permissionGroupId(1L)
            .build();
        // mock
        when(userPermissionGroupService.addOrUpdate(request)).thenReturn(true);
        // 执行
        boolean addOrUpdate = userPermissionGroupApplicationService.addOrUpdate(request);
        // 验证
        assert addOrUpdate;
    }

}
