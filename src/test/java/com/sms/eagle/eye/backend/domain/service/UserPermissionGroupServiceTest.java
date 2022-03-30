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
import com.sms.eagle.eye.backend.domain.mapper.UserPermissionGroupMapper;
import com.sms.eagle.eye.backend.domain.service.impl.UserPermissionGroupServiceImpl;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class UserPermissionGroupServiceTest {

    private final UserPermissionGroupMapper userPermissionGroupMapper = mock(UserPermissionGroupMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserPermissionGroupService userPermissionGroupService = spy(new UserPermissionGroupServiceImpl(objectMapper));

    @Test
    void getPermissionByEmail_test() {
        doReturn(userPermissionGroupMapper).when(userPermissionGroupService).getBaseMapper();
        String permission = "[\"permission\"]";
        when(userPermissionGroupMapper.getPermissionByEmail(anyString())).thenReturn(permission);
        Set<String> permissions = userPermissionGroupService.getPermissionByEmail("email@test.com");
        assertThat(permissions).hasSize(1);
    }

    @Test
    void addOrUpdate_test() {
        UserPermissionGroupRequest request = UserPermissionGroupRequest.builder().email("email@test.com").permissionGroupId(1L)
            .build();
        doReturn(null).when(userPermissionGroupService).getOne(any());
        doReturn(true).when(userPermissionGroupService).saveOrUpdate(any(UserPermissionGroupEntity.class));
        assert userPermissionGroupService.addOrUpdate(request);
    }

}
