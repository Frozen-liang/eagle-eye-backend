package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigMetadataConverter {

    ConfigMetadata fromChannelField(ChannelFieldResponse channelFieldResponse);

    ConfigMetadata fromConfigField(PluginConfigFieldEntity pluginConfigField);

    ConfigMetadata fromAlertField(PluginAlertFieldEntity pluginAlertFieldEntity);
}