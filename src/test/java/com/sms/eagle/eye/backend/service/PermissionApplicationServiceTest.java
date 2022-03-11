package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.service.PermissionService;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import com.sms.eagle.eye.backend.service.impl.PermissionApplicationServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PermissionApplicationServiceTest {

    private final PermissionService permissionService = mock(PermissionService.class);
    private final PermissionApplicationService permissionApplicationService =
        new PermissionApplicationServiceImpl(permissionService);
    private static final String NAME = "name";

    @Test
    void list_test() {
        when(permissionService.listByName(NAME)).thenReturn(Collections.emptyList());
        List<PermissionResponse> result = permissionApplicationService.list(NAME);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
