package com.sms.eagle.eye.backend.request.permission;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.model.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupQueryRequest extends PageRequest<PermissionGroupEntity> {

    private String name;
}
