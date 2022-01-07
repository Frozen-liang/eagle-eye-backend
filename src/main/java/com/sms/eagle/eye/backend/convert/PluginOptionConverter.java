package com.sms.eagle.eye.backend.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.entity.PluginSelectOptionEntity;
import com.sms.eagle.eye.plugin.v1.SelectOption;
import com.sms.eagle.eye.plugin.v1.SelectOptionItem;
import io.vavr.control.Try;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluginOptionConverter {

    @Mapping(source = "pluginSelectOption.items", target = "data")
    PluginSelectOptionEntity rpcToEntity(SelectOption pluginSelectOption, Long pluginId,
        @Context ObjectMapper objectMapper);

    default String toString(List<SelectOptionItem> items, @Context ObjectMapper objectMapper) {
        return Try.of(() -> objectMapper.writeValueAsString(items)).getOrElse("[]");
    }
}