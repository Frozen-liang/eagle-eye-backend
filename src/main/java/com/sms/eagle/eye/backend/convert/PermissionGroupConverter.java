package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionGroupConverter {

    PermissionGroupEntity toEntity(PermissionGroupRequest request);

    PermissionGroupResponse toResponse(PermissionGroupEntity entity);

    List<PermissionGroupResponse> toResponses(List<PermissionGroupEntity> entitys);
}
