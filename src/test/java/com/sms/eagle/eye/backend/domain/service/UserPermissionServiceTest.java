package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.UserPermissionMapper;
import com.sms.eagle.eye.backend.domain.service.impl.UserPermissionServiceImpl;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class UserPermissionServiceTest {

    private final UserPermissionMapper userPermissionMapper = mock(UserPermissionMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserPermissionService userPermissionService = spy(new UserPermissionServiceImpl(objectMapper));

    @Test
    void getPermissionByEmail_test() {
        doReturn(userPermissionMapper).when(userPermissionService).getBaseMapper();
        String permission = "[\"permission\"]";
        when(userPermissionMapper.getPermissionByEmail(anyString())).thenReturn(permission);
        Set<String> permissions = userPermissionService.getPermissionByEmail("email@test.com");
        assertThat(permissions).hasSize(1);
    }

    @Test
    void addOrUpdate_test() {
        UserPermissionGroupRequest request = UserPermissionGroupRequest.builder().email("email@test.com").permissionGroupId(1L)
            .build();
        doReturn(null).when(userPermissionService).getOne(any());
        doReturn(true).when(userPermissionService).saveOrUpdate(any(UserPermissionGroupEntity.class));
        assert userPermissionService.addOrUpdate(request);
    }

}
