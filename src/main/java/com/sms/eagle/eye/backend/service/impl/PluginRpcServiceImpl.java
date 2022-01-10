package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_SERVER_URL_ERROR;

import com.google.protobuf.Empty;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.factory.PluginClientFactory;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginMetadataResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.backend.service.PluginRpcService;
import com.sms.eagle.eye.plugin.v1.CreateTaskRequest;
import com.sms.eagle.eye.plugin.v1.CreateTaskResponse;
import com.sms.eagle.eye.plugin.v1.DeleteTaskRequest;
import com.sms.eagle.eye.plugin.v1.GeneralResponse;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import com.sms.eagle.eye.plugin.v1.UpdateTaskRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PluginRpcServiceImpl implements PluginRpcService {

    private final PluginClientFactory factory;
    private final PluginConfigResolver pluginConfigResolver;
    private final PluginConfigFieldConverter pluginConfigFieldConverter;

    public PluginRpcServiceImpl(PluginClientFactory factory,
        PluginConfigResolver pluginConfigResolver,
        PluginConfigFieldConverter pluginConfigFieldConverter) {
        this.factory = factory;
        this.pluginConfigResolver = pluginConfigResolver;
        this.pluginConfigFieldConverter = pluginConfigFieldConverter;
    }

    @Override
    public RegisterResponse getRegisterResponseByTarget(String target) {
        try {
            return factory.getClient(target)
                .getBlockingStub().fetchMetadata(Empty.newBuilder().build());
        } catch (Exception exception) {
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
            .options(options)
            .scheduleBySelf(registerResponse.getScheduleBySelf())
            .build();
    }

    // TODO
    @Override
    public CreateTaskResponse createTask(TaskEntity task, PluginEntity plugin, List<PluginConfigFieldEntity> fields) {
        Integer minuteInterval = convertToMinuteInterval(task);
        String config = pluginConfigResolver.decryptToString(fields, task.getPluginConfig());
        CreateTaskRequest request = CreateTaskRequest.newBuilder()
            .setId(task.getId().toString())
            .setName(task.getName())
            .setDescription(task.getDescription())
            .setInterval(minuteInterval)
            .setConfig(config)
            .build();
        return factory.getClient(plugin.getUrl()).getBlockingStub().createOrExecute(request);
    }

    @Override
    public GeneralResponse removeTask(String mappingId, TaskEntity task, PluginEntity plugin,
        List<PluginConfigFieldEntity> fields) {
        String config = pluginConfigResolver.decryptToString(fields, task.getPluginConfig());
        return factory.getClient(plugin.getUrl()).getBlockingStub()
            .remove(DeleteTaskRequest.newBuilder().setMappingId(mappingId)
                .setConfig(config).build());
    }

    @Override
    public GeneralResponse updateTask(String mappingId, TaskEntity task, PluginEntity plugin,
        List<PluginConfigFieldEntity> fields) {
        Integer minuteInterval = convertToMinuteInterval(task);
        String config = pluginConfigResolver.decryptToString(fields, task.getPluginConfig());
        UpdateTaskRequest request = UpdateTaskRequest.newBuilder()
            .setMappingId(mappingId)
            .setName(task.getName())
            .setDescription(task.getDescription())
            .setInterval(minuteInterval)
            .setConfig(config)
            .build();
        return factory.getClient(plugin.getUrl()).getBlockingStub().edit(request);
    }

    private Integer convertToMinuteInterval(TaskEntity task) {
        return TaskScheduleUnit.resolve(task.getScheduleUnit())
            .getConvertToMinute().apply(task.getScheduleInterval());
    }
}