package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.AddOrRemovePermissionRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.service.impl.PermissionGroupApplicationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PermissionGroupApplicationServiceTest {

    private final PermissionGroupService permissionGroupService = mock(PermissionGroupService.class);
    private final PermissionGroupApplicationService permissionGroupApplicationService =
        new PermissionGroupApplicationServiceImpl(permissionGroupService);
    private final PermissionGroupRequest request = PermissionGroupRequest.builder().id(1L).name("name").build();
    private final AddOrRemovePermissionRequest addOrRemovePermissionRequest =
        AddOrRemovePermissionRequest.builder().groupId(ID).permission("permission").build();
    private static final Long ID = 1L;

    @Test
    void page_test() {
        // mock
        PermissionGroupQueryRequest request = PermissionGroupQueryRequest.builder().build();
        IPage<PermissionGroupResponse> page = mock(IPage.class);
        List<PermissionGroupResponse> list = mock(List.class);
        doReturn(list).when(page).getRecords();
        when(permissionGroupService.page(request)).thenReturn(page);
        // 执行
        CustomPage<PermissionGroupResponse> result = permissionGroupApplicationService.page(request);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).isEqualTo(list);
    }

    @Test
    void save_test() {
        // mock
        when(permissionGroupService.saveFromRequest(request)).thenReturn(ID);
        // 执行
        String result = permissionGroupApplicationService.save(request);
        // 验证
        assertThat(result).isEqualTo(ID.toString());
    }

    @Test
    void update_test() {
        // mock
        when(permissionGroupService.updateFromRequest(request)).thenReturn(true);
        // 执行
        boolean update = permissionGroupApplicationService.update(request);
        // 验证
        assertThat(update).isTrue();
    }

    @Test
    void delete_test() {
        // mock

        when(permissionGroupApplicationService.delete(ID)).thenReturn(true);
        // 执行
        boolean delete = permissionGroupApplicationService.delete(ID);
        // 验证
        assertThat(delete).isTrue();
    }

    @Test
    void addPermission_true_test() {
        // mock
        when(permissionGroupService.getOne(addOrRemovePermissionRequest.getGroupId()))
            .thenReturn(PermissionGroupEntity.builder().build());
        // 执行
        boolean result = permissionGroupApplicationService.addPermission(addOrRemovePermissionRequest);
        // 验证
        assertThat(result).isTrue();
    }


    @Test
    void removePermission_test() {
        // mock
        when(permissionGroupService.getOne(addOrRemovePermissionRequest.getGroupId()))
            .thenReturn(PermissionGroupEntity.builder().build());
        // 验证
        assert permissionGroupApplicationService.removePermission(addOrRemovePermissionRequest);
    }

    @Test
    void list_test() {
        // mock
        List<PermissionGroupResponse> list = mock(List.class);
        when(permissionGroupService.queryAll()).thenReturn(list);
        List<PermissionGroupResponse> result = permissionGroupApplicationService.list();
        assertThat(result).isEqualTo(list);
    }

}
