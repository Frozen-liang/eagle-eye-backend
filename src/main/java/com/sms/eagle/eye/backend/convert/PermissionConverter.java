package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.permission.PermissionEntity;
import com.sms.eagle.eye.backend.request.permission.PermissionRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionConverter {

    PermissionEntity toEntity(PermissionRequest request);

    PermissionResponse toResponse(PermissionEntity entity);

    List<PermissionResponse> toResponses(List<PermissionEntity> entities);
}
