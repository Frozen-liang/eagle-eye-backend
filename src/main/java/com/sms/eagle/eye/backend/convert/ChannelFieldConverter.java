package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelFieldConverter {

    ChannelFieldWithValueResponse fillValueToResponse(ChannelFieldResponse channelFieldResponse, Object value);
}
