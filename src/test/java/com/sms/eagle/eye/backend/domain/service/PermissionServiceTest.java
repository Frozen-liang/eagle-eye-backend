package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.convert.PermissionConverter;
import com.sms.eagle.eye.backend.convert.PermissionConverterImpl;
import com.sms.eagle.eye.backend.domain.mapper.PermissionMapper;
import com.sms.eagle.eye.backend.domain.service.impl.PermissionServiceImpl;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PermissionServiceTest {

    private final PermissionConverter permissionConverter = new PermissionConverterImpl();
    private final PermissionService permissionService = spy(new PermissionServiceImpl(permissionConverter));

    @Test
    void listByName_test() {
        doReturn(Collections.emptyList()).when(permissionService).list(any());
        List<PermissionResponse> result = permissionService.listByName("name");
        assertThat(result).isEmpty();
    }

}
