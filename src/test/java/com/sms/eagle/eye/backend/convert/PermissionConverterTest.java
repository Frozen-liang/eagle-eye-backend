package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionEntity;
import com.sms.eagle.eye.backend.request.permission.PermissionRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class PermissionConverterTest {

    private final PermissionConverter permissionConverter = new PermissionConverterImpl();

    @Test
    void toEntity_test() {
        String name = "name";
        PermissionRequest request = PermissionRequest.builder().name(name).build();
        PermissionEntity permissionEntity = permissionConverter.toEntity(request);
        assertThat(permissionEntity.getName()).isEqualTo(name);
    }

    @Test
    void toEntity_null_test() {
        PermissionEntity permissionEntity = permissionConverter.toEntity(null);
        assertThat(permissionEntity).isNull();
    }

    @Test
    void toResponse_null_test() {
        assertThat(permissionConverter.toResponse(null)).isNull();
    }

    @Test
    void toResponses_test() {
        int size = 10;
        Long id = 1L;
        List<PermissionEntity> permissionEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            permissionEntities.add(PermissionEntity.builder().id(id).build());
        }
        List<PermissionResponse> result = permissionConverter.toResponses(permissionEntities);
        assertThat(result).hasSize(size).anyMatch(permissionResponse -> id.equals(permissionResponse.getId()));
    }

}
