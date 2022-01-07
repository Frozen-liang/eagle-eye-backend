package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import com.sms.eagle.eye.plugin.v1.FieldType;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluginConfigFieldConverter {

    PluginConfigFieldEntity rpcToEntity(ConfigField pluginField, Long pluginId);

    PluginConfigFieldResponse rpcToResponse(ConfigField pluginField);

    PluginConfigFieldResponse toResponse(PluginConfigFieldEntity pluginConfigFieldEntity);

    PluginConfigFieldWithValueResponse toValueResponse(PluginConfigFieldEntity pluginConfigFieldEntity, String value);

    default Integer map(FieldType value) {
        return Objects.nonNull(value) ? value.getNumber() : FieldType.FIELD_UNKNOWN_VALUE;
    }
}