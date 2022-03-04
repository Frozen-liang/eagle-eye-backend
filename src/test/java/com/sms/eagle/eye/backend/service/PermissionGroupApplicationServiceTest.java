package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupConnEntity;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupConnService;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupConnRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.service.PermissionApplicationService;
import com.sms.eagle.eye.backend.service.PermissionGroupApplicationService;
import com.sms.eagle.eye.backend.service.impl.PermissionApplicationServiceImpl;
import com.sms.eagle.eye.backend.service.impl.PermissionGroupApplicationServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PermissionGroupApplicationServiceTest {

    private final PermissionGroupService permissionGroupService = mock(PermissionGroupService.class);
    private final PermissionGroupConnService permissionGroupConnService = mock(PermissionGroupConnService.class);
    private final PermissionGroupApplicationService permissionGroupApplicationService =
        new PermissionGroupApplicationServiceImpl(permissionGroupService, permissionGroupConnService);
    private final PermissionGroupRequest request = PermissionGroupRequest.builder().id(1L).name("name").build();
    private final PermissionGroupConnRequest permissionGroupConnRequest =
        PermissionGroupConnRequest.builder().groupId(ID).permissionId(ID).build();
    private static final Long ID = 1L;

    @Test
    void page_test() {
        PermissionGroupQueryRequest request = PermissionGroupQueryRequest.builder().build();
        when(permissionGroupService.page(request)).thenReturn(new Page<>());
        CustomPage<PermissionGroupResponse> result = permissionGroupApplicationService.page(request);
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).isEmpty();
    }

    @Test
    void save_test() {
        when(permissionGroupService.saveFromRequest(request)).thenReturn(ID);
        String result = permissionGroupApplicationService.save(request);
        assertThat(result).isEqualTo(ID.toString());
    }

    @Test
    void update_test() {
        when(permissionGroupService.updateFromRequest(request)).thenReturn(true);
        assert permissionGroupApplicationService.update(request);
    }

    @Test
    void delete_test() {
        when(permissionGroupConnService.remove(any())).thenReturn(true);
        when(permissionGroupApplicationService.delete(ID)).thenReturn(true);
        assert permissionGroupApplicationService.delete(ID);
    }

    @Test
    void addPermission_true_test() {
        when(permissionGroupConnService.count(any())).thenReturn(0L);
        when(permissionGroupConnService.save(any(PermissionGroupConnEntity.class))).thenReturn(true);
        assert permissionGroupApplicationService.addPermission(permissionGroupConnRequest);
    }

    @Test
    void addPermission_false_test() {
        when(permissionGroupConnService.count(any())).thenReturn(1L);
        when(permissionGroupConnService.save(any(PermissionGroupConnEntity.class))).thenReturn(true);
        assertThat(permissionGroupApplicationService.addPermission(permissionGroupConnRequest)).isFalse();
    }

    @Test
    void removePermission_test() {
        when(permissionGroupConnService.remove(any())).thenReturn(true);
        assert permissionGroupApplicationService.removePermission(permissionGroupConnRequest);
    }

    @Test
    void list_test() {
        when(permissionGroupService.list()).thenReturn(Collections.emptyList());
        List<PermissionGroupResponse> result = permissionGroupApplicationService.list();
        assertThat(result).isEmpty();
    }

}
