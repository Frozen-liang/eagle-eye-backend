package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_SERVER_URL_ERROR;

import com.google.protobuf.Empty;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.factory.PluginClientFactory;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginMetadataResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.backend.service.PluginRpcService;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PluginRpcServiceImpl implements PluginRpcService {

    private final PluginClientFactory factory;
    private final PluginConfigFieldConverter pluginConfigFieldConverter;

    public PluginRpcServiceImpl(PluginClientFactory factory,
        PluginConfigFieldConverter pluginConfigFieldConverter) {
        this.factory = factory;
        this.pluginConfigFieldConverter = pluginConfigFieldConverter;
    }

    @Override
    public RegisterResponse getRegisterResponseByTarget(String target) {
        try {
            return factory.getClient(target)
                .getBlockingStub().fetchMetadata(Empty.newBuilder().build());
        } catch (Exception exception) {
            factory.removeClient(target);
            log.error(PLUGIN_SERVER_URL_ERROR.getMessage(), exception);
            throw new EagleEyeException(PLUGIN_SERVER_URL_ERROR);
        }
    }

    @Override
    public PluginMetadataResponse getMetadataResponseByTarget(String target) {
        RegisterResponse registerResponse = getRegisterResponseByTarget(target);
        List<PluginConfigFieldResponse> configFieldList = registerResponse.getFieldsList().stream()
            .map(pluginConfigFieldConverter::rpcToResponse)
            .collect(Collectors.toList());
        List<PluginConfigFieldResponse> alertFieldList = registerResponse.getAlertsList().stream()
            .map(pluginConfigFieldConverter::rpcToResponse)
            .collect(Collectors.toList());
        List<PluginSelectOptionResponse> options = registerResponse.getOptionsList().stream()
            .map(option -> PluginSelectOptionResponse.builder()
                .key(option.getKey())
                .data(option.getItemsList().stream()
                    .map(optionItem -> PluginSelectOptionResponse.PluginSelectOptionItemResponse.builder()
                        .label(optionItem.getLabel())
                        .value(optionItem.getValue()).build())
                    .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
        return PluginMetadataResponse.builder()
            .name(registerResponse.getName())
            .description(registerResponse.getDescription())
            .version(registerResponse.getVersion())
            .fields(configFieldList)
            .alerts(alertFieldList)
            .options(options)
            .scheduleBySelf(registerResponse.getScheduleBySelf())
            .build();
    }
}