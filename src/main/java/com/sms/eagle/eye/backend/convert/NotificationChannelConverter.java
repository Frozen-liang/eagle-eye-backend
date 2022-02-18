package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationChannelConverter {

    @Mapping(target = "type", qualifiedByName = "getTypeName")
    ChannelListResponse toResponse(NotificationChannelEntity entity);

    @Named("getTypeName")
    default String getTypeName(Integer type) {
        return NotificationChannelType.resolve(type).getName();
    }

}
