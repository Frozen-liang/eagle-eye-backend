package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class PermissionGroupConverterTest {

    private final PermissionGroupConverter permissionGroupConverter = new PermissionGroupConverterImpl();

    @Test
    void toEntity_null_test() {
        assertThat(permissionGroupConverter.toEntity(null)).isNull();
    }

    @Test
    void toEntity_test() {
        String name = "name";
        PermissionGroupRequest request = PermissionGroupRequest.builder().name(name).build();
        assertThat(permissionGroupConverter.toEntity(request).getName()).isEqualTo(name);
    }

}
