package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.convert.PermissionGroupConverter;
import com.sms.eagle.eye.backend.convert.PermissionGroupConverterImpl;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.PermissionGroupMapper;
import com.sms.eagle.eye.backend.domain.service.impl.PermissionGroupServiceImpl;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PermissionGroupServiceTest {

    private final PermissionGroupMapper permissionGroupMapper = mock(PermissionGroupMapper.class);
    private final PermissionGroupConverter permissionGroupConverter = new PermissionGroupConverterImpl();
    private final PermissionGroupService permissionGroupService =
        spy(new PermissionGroupServiceImpl(permissionGroupConverter));
    private static final Long ID = 1L;
    private final PermissionGroupRequest request = PermissionGroupRequest.builder().id(ID).name("name").build();



    @Test
    void page_test() {
        PermissionGroupQueryRequest request = PermissionGroupQueryRequest.builder().build();
        when(permissionGroupService.getBaseMapper()).thenReturn(permissionGroupMapper);
        IPage<PermissionGroupResponse> page = new Page<>();
        when(permissionGroupMapper.page(any(), any(PermissionGroupQueryRequest.class))).thenReturn(page);
        IPage<PermissionGroupResponse> result = permissionGroupService.page(request);
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).isEmpty();
    }

    @Test
    void saveFromRequest_test() {
        doReturn(true).when(permissionGroupService).save(any(PermissionGroupEntity.class));
        permissionGroupService.saveFromRequest(request);
        verify(permissionGroupService).save(any(PermissionGroupEntity.class));
    }

    @Test
    void updateFromRequest_test() {
        doReturn(true).when(permissionGroupService).updateById(any(PermissionGroupEntity.class));
        assert permissionGroupService.updateFromRequest(request);
    }

    @Test
    void deleteById_test() {
        doReturn(true).when(permissionGroupService).removeById(ID);
        assert permissionGroupService.deleteById(ID);
    }

    @Test
    void queryAll_test() {
        doReturn(Collections.emptyList()).when(permissionGroupService).list();
        List<PermissionGroupResponse> result = permissionGroupService.queryAll();
        assertThat(result).isEmpty();
    }
}
