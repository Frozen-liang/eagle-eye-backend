package com.sms.eagle.eye.backend.handler.impl;

import static com.sms.eagle.eye.backend.utils.TaskScheduleUtil.getMinuteInterval;

import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.factory.PluginClientFactory;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import com.sms.eagle.eye.plugin.v1.AlertRule;
import com.sms.eagle.eye.plugin.v1.CreateTaskRequest;
import com.sms.eagle.eye.plugin.v1.CreateTaskResponse;
import com.sms.eagle.eye.plugin.v1.DeleteTaskRequest;
import com.sms.eagle.eye.plugin.v1.GeneralResponse;
import com.sms.eagle.eye.plugin.v1.UpdateTaskRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcTaskHandler implements TaskHandler {

    private final PluginClientFactory factory;
    private final TaskService taskService;
    private final ThirdPartyMappingService thirdPartyMappingService;
    private final InvokeErrorRecordService invokeErrorRecordService;

    public GrpcTaskHandler(PluginClientFactory factory,
        TaskService taskService,
        ThirdPartyMappingService thirdPartyMappingService,
        InvokeErrorRecordService invokeErrorRecordService) {
        this.factory = factory;
        this.taskService = taskService;
        this.thirdPartyMappingService = thirdPartyMappingService;
        this.invokeErrorRecordService = invokeErrorRecordService;
    }

    @Override
    public void startTask(TaskOperationRequest request) {
        List<AlertRule> rules = request.getAlertRules().stream().map(taskAlertRule -> AlertRule.newBuilder()
            .setAlarmLevel(taskAlertRule.getAlarmLevel())
            .setRule(taskAlertRule.getDecryptedAlertRule())
            .setInterval(getMinuteInterval(taskAlertRule))
            .build()).collect(Collectors.toList());
        CreateTaskRequest grpcRequest = CreateTaskRequest.newBuilder()
            .setId(request.getTask().getId().toString())
            .setName(request.getTask().getName())
            .setDescription(request.getTask().getDescription())
            .setConfig(request.getDecryptedConfig())
            .addAllRules(rules)
            .build();
        try {
            CreateTaskResponse response = factory.getClient(request.getPlugin().getUrl())
                .getBlockingStub().createOrExecute(grpcRequest);
            log.info("CreateTaskResponse: {}", response);
            if (!Objects.equals(request.getTask().getId().toString(), response.getId())) {
                thirdPartyMappingService.addPluginSystemUnionIdMapping(request.getTask().getId(), response.getId());
            }
        } catch (Exception exception) {
            log.error("", exception);
            invokeErrorRecordService.addErrorRecord(request.getTask().getId(), exception.getMessage());
            taskService.updateTaskEntity(TaskEntity.builder()
                .id(request.getTask().getId())
                .status(TaskStatus.ERROR.getValue())
                .build());
            throw exception;
        }

    }

    @Override
    public void stopTask(TaskOperationRequest request) {
        Optional<String> mappingIdOptional = thirdPartyMappingService.getPluginSystemUnionId(request.getTask().getId());
        try {
            GeneralResponse response = factory.getClient(request.getPlugin().getUrl()).getBlockingStub()
                .remove(DeleteTaskRequest.newBuilder()
                    .setMappingId(mappingIdOptional.orElse(request.getTask().getId().toString()))
                    .setConfig(request.getDecryptedConfig())
                    .build());
            log.info("GeneralResponse: {}", response);
        } catch (Exception exception) {
            log.error("", exception);
            throw exception;
        }
    }

    @Override
    public void updateTask(TaskOperationRequest request) {
        Optional<String> mappingIdOptional = thirdPartyMappingService.getPluginSystemUnionId(request.getTask().getId());
        try {
            List<AlertRule> rules = request.getAlertRules().stream().map(taskAlertRule -> AlertRule.newBuilder()
                .setAlarmLevel(taskAlertRule.getAlarmLevel())
                .setRule(taskAlertRule.getDecryptedAlertRule())
                .setInterval(getMinuteInterval(taskAlertRule))
                .build()).collect(Collectors.toList());
            UpdateTaskRequest grpcRequest = UpdateTaskRequest.newBuilder()
                .setMappingId(mappingIdOptional.orElse(request.getTask().getId().toString()))
                .setName(request.getTask().getName())
                .setDescription(request.getTask().getDescription())
                .addAllRules(rules)
                .setConfig(request.getDecryptedConfig())
                .build();
            GeneralResponse response = factory.getClient(request.getPlugin().getUrl())
                .getBlockingStub().edit(grpcRequest);
            log.info("GeneralResponse: {}", response);
        } catch (Exception exception) {
            log.error("", exception);
            throw exception;
        }
    }

    @Override
    public Boolean ifScheduleBySelf() {
        return Boolean.TRUE;
    }

}