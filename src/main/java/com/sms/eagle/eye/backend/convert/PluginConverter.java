package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PluginConverter {

    PluginEntity rpcResponseToEntity(RegisterResponse registerResponse, String url, String creator);
}
