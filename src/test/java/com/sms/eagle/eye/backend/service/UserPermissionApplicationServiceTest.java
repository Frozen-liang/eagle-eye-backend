package com.sms.eagle.eye.backend.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;
import com.sms.eagle.eye.backend.service.impl.UserPermissionApplicationServiceImpl;
import org.junit.jupiter.api.Test;

public class UserPermissionApplicationServiceTest {

    private final UserPermissionService userPermissionService = mock(UserPermissionService.class);
    private final UserPermissionApplicationService userPermissionApplicationService =
        new UserPermissionApplicationServiceImpl(userPermissionService);

    @Test
    void addOrUpdate_test() {
        // 构建请求参数
        UserPermissionRequest request = UserPermissionRequest.builder()
            .email("email@test.com")
            .permissionGroupId(1L)
            .build();
        // mock
        when(userPermissionService.addOrUpdate(request)).thenReturn(true);
        // 执行
        boolean addOrUpdate = userPermissionApplicationService.addOrUpdate(request);
        // 验证
        assert addOrUpdate;
    }

}
