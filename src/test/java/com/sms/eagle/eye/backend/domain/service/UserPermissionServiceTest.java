package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionEntity;
import com.sms.eagle.eye.backend.domain.mapper.UserPermissionMapper;
import com.sms.eagle.eye.backend.domain.service.impl.UserPermissionServiceImpl;
import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UserPermissionServiceTest {

    private final UserPermissionMapper userPermissionMapper = mock(UserPermissionMapper.class);
    private final UserPermissionService userPermissionService = spy(new UserPermissionServiceImpl());

    @Test
    void getPermissionByEmail_test() {
        doReturn(userPermissionMapper).when(userPermissionService).getBaseMapper();
        String permission = "permission";
        when(userPermissionMapper.getPermissionByEmail(anyString())).thenReturn(List.of(permission));
        List<String> permissions = userPermissionService.getPermissionByEmail("email@test.com");
        assertThat(permissions).hasSize(1).anyMatch(permission::equals);
    }

    /*@Test
    void getAllUserPermissionGroupName_test() {
        doReturn(userPermissionMapper).when(userPermissionService).getBaseMapper();
        String permissionGroupName = "permissionGroupName";
        UserPermissionGroupResponse userPermissionGroupResponse = UserPermissionGroupResponse.builder()
            .permissionGroupName(permissionGroupName).build();
        when(userPermissionMapper.getAllUserPermissionGroupName()).thenReturn(List.of(userPermissionGroupResponse));
        List<UserPermissionGroupResponse> result = userPermissionService
            .getAllUserPermissionGroupName();
        assertThat(result).hasSize(1).anyMatch(p -> permissionGroupName.equals(p.getPermissionGroupName()));
    }*/

    @Test
    void addOrUpdate_test() {
        UserPermissionRequest request = UserPermissionRequest.builder().email("email@test.com").permissionGroupId(1L)
            .build();
        doReturn(null).when(userPermissionService).getOne(any());
        doReturn(true).when(userPermissionService).saveOrUpdate(any(UserPermissionEntity.class));
        assert userPermissionService.addOrUpdate(request);
    }

}
