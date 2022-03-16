package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskAlertNotificationChannelConverter {

    @Mapping(target = "alertNotificationId", source = "id")
    TaskAlertNotificationResponse toResponse(TaskAlertNotificationEntity entity);
}
