package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigRuleResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import com.sms.eagle.eye.plugin.v1.FieldType;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluginAlertFieldConverter {

    PluginAlertFieldEntity rpcToEntity(ConfigField pluginField, Long pluginId);

    PluginConfigRuleResponse toResponse(PluginAlertFieldEntity pluginAlertFieldEntity);

    default Integer map(FieldType value) {
        return Objects.nonNull(value) ? value.getNumber() : FieldType.FIELD_UNKNOWN_VALUE;
    }
}