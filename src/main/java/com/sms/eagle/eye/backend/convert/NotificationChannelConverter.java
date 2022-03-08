package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationChannelConverter {

    @Mapping(target = "type", qualifiedByName = "getTypeName")
    ChannelPageResponse toResponse(NotificationChannelEntity entity);

    @Mapping(target = "config", source = "valueFieldResponse")
    ChannelDetailResponse toDetailResponse(NotificationChannelEntity entity,
        List<ChannelFieldWithValueResponse> valueFieldResponse);

    ChannelListResponse toListResponse(NotificationChannelEntity entity);

    NotificationChannelEntity toEntity(NotificationChannelRequest request);

    @Named("getTypeName")
    default String getTypeName(Integer type) {
        return NotificationChannelType.resolve(type).getName();
    }

}
