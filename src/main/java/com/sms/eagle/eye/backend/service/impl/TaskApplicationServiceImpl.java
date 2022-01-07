package com.sms.eagle.eye.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.TaskTagMappingService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.request.task.TaskScheduleRequest;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TaskApplicationServiceImpl implements TaskApplicationService {

    private final PluginConfigResolver pluginConfigResolver;
    private final TaskService taskService;
    private final AwsOperation awsOperation;
    private final PluginService pluginService;
    private final PluginConfigFieldService pluginConfigFieldService;
    private final PluginSelectOptionService pluginSelectOptionService;
    private final TaskTagMappingService taskTagMappingService;
    private final InvokeErrorRecordService invokeErrorRecordService;
    private final ThirdPartyMappingService thirdPartyMappingService;

    public TaskApplicationServiceImpl(PluginConfigResolver pluginConfigResolver, TaskService taskService,
        AwsOperation awsOperation, PluginService pluginService,
        PluginConfigFieldService pluginConfigFieldService,
        PluginSelectOptionService pluginSelectOptionService,
        TaskTagMappingService taskTagMappingService,
        InvokeErrorRecordService invokeErrorRecordService,
        ThirdPartyMappingService thirdPartyMappingService) {
        this.pluginConfigResolver = pluginConfigResolver;
        this.taskService = taskService;
        this.awsOperation = awsOperation;
        this.pluginService = pluginService;
        this.pluginConfigFieldService = pluginConfigFieldService;
        this.pluginSelectOptionService = pluginSelectOptionService;
        this.taskTagMappingService = taskTagMappingService;
        this.invokeErrorRecordService = invokeErrorRecordService;
        this.thirdPartyMappingService = thirdPartyMappingService;
    }

    @Override
    public CustomPage<TaskResponse> page(TaskQueryRequest request) {
        IPage<TaskResponse> page = taskService.getPage(request);
        page.convert(taskResponse -> {
            taskResponse.setTagList(taskTagMappingService.getTagListByTaskId(taskResponse.getId()));
            return taskResponse;
        });
        return new CustomPage<>(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addTask(TaskBasicInfoRequest request) {
        Long taskId = taskService.saveFromRequest(request);
        taskTagMappingService.updateTagMapping(taskId, request.getTagList());
        return taskId.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTask(TaskBasicInfoRequest request) {
        taskService.updateFromRequest(request);
        taskTagMappingService.updateTagMapping(request.getId(), request.getTagList());
        return true;
    }

    @Override
    public TaskPluginConfigResponse getPluginConfigByTaskId(Long taskId) {
        TaskEntity taskEntity = taskService.getEntityById(taskId);
        List<PluginConfigFieldEntity> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId());
        return TaskPluginConfigResponse.builder()
            .fields(pluginConfigResolver.decryptToResponse(configFieldList, taskEntity.getPluginConfig()))
            .options(pluginSelectOptionService.getResponseByPluginId(taskEntity.getPluginId()))
            .build();
    }

    @Override
    public boolean updatePluginConfig(TaskPluginConfigRequest request) {
        TaskEntity taskEntity = taskService.getEntityById(request.getId());
        List<PluginConfigFieldEntity> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId());
        taskEntity.setPluginConfig(pluginConfigResolver
            .checkAndEncrypt(configFieldList, request.getPluginConfig()));
        taskService.updateTaskEntity(taskEntity);
        return true;
    }

    @Override
    public boolean updateSchedule(TaskScheduleRequest request) {
        taskService.updateTaskEntity(TaskEntity.builder()
            .id(request.getId())
            .scheduleUnit(request.getScheduleUnit())
            .scheduleInterval(request.getScheduleInterval())
            .build());
        return true;
    }

    @Override
    public boolean startByTaskId(Long taskId) {
        TaskEntity task = taskService.getEntityById(taskId);
        PluginEntity plugin = pluginService.getEntityById(task.getPluginId());
        List<PluginConfigFieldEntity> configFields = pluginConfigFieldService.getListByPluginId(plugin.getId());
        if (Objects.equals(plugin.getScheduleBySelf(), Boolean.TRUE)) {
            log.info("执行rpc");
        } else {
            Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(taskId);
            awsRuleOptional.ifPresentOrElse(ruleArn -> {
                log.info("awsOperation, update and enable ");
            }, () -> {
                String ruleArn = awsOperation.createRuleAndReturnArn(task);
                awsOperation.createRuleTargetAndReturnId(task, plugin, configFields);
                thirdPartyMappingService.addAwsRuleMapping(taskId, ruleArn);
            });
        }
        return true;
    }

    @Override
    public boolean stopByTaskId(Long taskId) {
        TaskEntity task = taskService.getEntityById(taskId);
        PluginEntity plugin = pluginService.getEntityById(task.getPluginId());
        pluginConfigFieldService.getListByPluginId(plugin.getId());
        if (Objects.equals(plugin.getScheduleBySelf(), Boolean.TRUE)) {
            log.info("执行rpc");
        } else {
            Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(taskId);
            awsRuleOptional.ifPresentOrElse(ruleArn -> {
                log.info("stop event bridge, {}", ruleArn);
            }, () -> {
                log.info("stop error");
            });
        }
        return true;
    }

    @Override
    public boolean removeTask(Long taskId) {
        taskService.deleteTaskById(taskId);
        return true;
    }

    @Override
    public boolean resolveInvokeResult(LambdaInvokeResult request) {
        if (Objects.equals(request.getSuccess(), Boolean.FALSE)) {
            invokeErrorRecordService.addErrorRecord(request);
            taskService.updateTaskEntity(TaskEntity.builder()
                .id(request.getTaskId())
                .status(TaskStatus.ERROR.getValue())
                .build());
        } else {
            if (!Objects.equals(request.getTaskId().toString(), request.getMappingId())) {
                thirdPartyMappingService.addPluginSystemUnionIdMapping(request);
            }
        }
        return true;
    }

}