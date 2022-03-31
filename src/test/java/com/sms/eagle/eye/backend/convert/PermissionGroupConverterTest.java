package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class PermissionGroupConverterTest {

    private final PermissionGroupConverter permissionGroupConverter = new PermissionGroupConverterImpl();
    private final PermissionGroupEntity entity = mock(PermissionGroupEntity.class);
    private final PermissionGroupResponse response = mock(PermissionGroupResponse.class);
    private final List<PermissionGroupEntity> entityList = mock(List.class);

    private final String name = "name";
    private final int SIZE = 5;

    @Test
    void toEntity_null_test() {
        assertThat(permissionGroupConverter.toEntity(null)).isNull();
    }

    @Test
    void toEntity_test() {
        PermissionGroupRequest request = PermissionGroupRequest.builder().name(name).build();
        assertThat(permissionGroupConverter.toEntity(request).getName()).isEqualTo(name);
    }

    @Test
    void toResponse_null_test() {
        assertThat(permissionGroupConverter.toResponse(null)).isNull();
    }

    @Test
    void toResponse_set_null_test() {
        when(entity.getPermissions()).thenReturn(null);
        when(entity.getName()).thenReturn(name);
        PermissionGroupResponse response = permissionGroupConverter.toResponse(entity);
        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    void toResponse_set_not_null_test() {
        HashSet<String> strings = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            strings.add(name);
        }
        when(entity.getPermissions()).thenReturn(strings);
        when(response.getPermissions()).thenReturn(strings);
        when(entity.getName()).thenReturn(name);
        PermissionGroupResponse response = permissionGroupConverter.toResponse(entity);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getPermissions()).isEqualTo(strings);
    }

    @Test
    void toResponses_null_test() {
        assertThat(permissionGroupConverter.toResponses(null)).isNull();
    }

    @Test
    void toResponses_test() {
        PermissionGroupEntity groupEntity = PermissionGroupEntity.builder().name(name).build();
        List<PermissionGroupEntity> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            list.add(groupEntity);
        }

        List<PermissionGroupResponse> responses = permissionGroupConverter.toResponses(list);
        assertThat(responses).isNotEmpty();
        assertThat(responses).hasSize(SIZE);
        assertThat(responses).allMatch(item -> Objects.equals(item.getName(),name));
    }
}
