package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_ALREADY_NON_RUNNING;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_ALREADY_RUNNING;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_RUNNING_AND_DELETE_ERROR;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_NAME_HAS_ALREADY_EXIST;
import static com.sms.eagle.eye.backend.handler.impl.TaskHandlerProxy.TASK_HANDLER_PROXY;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.PluginAlertFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TaskAlertRuleService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.TaskTagMappingService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.task.TaskAlertRuleRequest;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.request.task.TaskScheduleRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.task.PluginConfigRuleWithValueResponse;
import com.sms.eagle.eye.backend.response.task.TaskAlertRuleResponse;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TaskApplicationServiceImpl implements TaskApplicationService {

    private final TaskHandler taskHandler;
    private final ConfigMetadataConverter configMetadataConverter;
    private final ConfigMetadataResolver configMetadataResolver;
    private final TaskService taskService;
    private final PluginService pluginService;
    private final InvokeErrorRecordService invokeErrorRecordService;
    private final PluginConfigFieldService pluginConfigFieldService;
    private final PluginAlertFieldService pluginAlertFieldService;
    private final PluginSelectOptionService pluginSelectOptionService;
    private final TaskTagMappingService taskTagMappingService;
    private final TaskGroupService taskGroupService;
    private final TaskGroupMappingService taskGroupMappingService;
    private final TaskAlertRuleService taskAlertRuleService;

    public TaskApplicationServiceImpl(@Qualifier(TASK_HANDLER_PROXY) TaskHandler taskHandler,
        ConfigMetadataConverter configMetadataConverter,
        ConfigMetadataResolver configMetadataResolver, TaskService taskService,
        PluginService pluginService,
        InvokeErrorRecordService invokeErrorRecordService,
        PluginConfigFieldService pluginConfigFieldService,
        PluginAlertFieldService pluginAlertFieldService,
        PluginSelectOptionService pluginSelectOptionService,
        TaskTagMappingService taskTagMappingService,
        TaskGroupService taskGroupService,
        TaskGroupMappingService taskGroupMappingService,
        TaskAlertRuleService taskAlertRuleService) {
        this.taskHandler = taskHandler;
        this.configMetadataConverter = configMetadataConverter;
        this.configMetadataResolver = configMetadataResolver;
        this.taskService = taskService;
        this.pluginService = pluginService;
        this.invokeErrorRecordService = invokeErrorRecordService;
        this.pluginConfigFieldService = pluginConfigFieldService;
        this.pluginAlertFieldService = pluginAlertFieldService;
        this.pluginSelectOptionService = pluginSelectOptionService;
        this.taskTagMappingService = taskTagMappingService;
        this.taskGroupService = taskGroupService;
        this.taskGroupMappingService = taskGroupMappingService;
        this.taskAlertRuleService = taskAlertRuleService;
    }

    @Override
    public CustomPage<TaskResponse> page(TaskQueryRequest request) {
        IPage<TaskResponse> page = taskService.getPage(request, getSelfAndChildGroupList(request));
        page.convert(taskResponse -> {
            taskResponse.setTagList(taskTagMappingService.getTagListByTaskId(taskResponse.getId()));
            taskResponse.setGroupList(taskGroupMappingService.getGroupListByTaskId(taskResponse.getId()));
            return taskResponse;
        });
        return new CustomPage<>(page);
    }

    private List<Long> getSelfAndChildGroupList(TaskQueryRequest request) {
        if (Objects.nonNull(request.getGroupId())) {
            List<Long> groups = taskGroupService.getChildGroupById(request.getGroupId());
            groups.add(request.getGroupId());
            return groups;
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addTask(TaskBasicInfoRequest request) {
        if (taskService.countByName(request.getName()) != 0) {
            // TODO Create unique index
            throw new EagleEyeException(TASK_NAME_HAS_ALREADY_EXIST);
        }
        Long taskId = taskService.saveFromRequest(request);
        taskTagMappingService.updateTagMapping(taskId, request.getTagList());
        taskGroupMappingService.updateGroupMapping(taskId, request.getGroupList());
        return taskId.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTask(TaskBasicInfoRequest request) {
        if (taskService.countByName(request.getName()) > 1) {
            throw new EagleEyeException(TASK_NAME_HAS_ALREADY_EXIST);
        }
        taskService.updateFromRequest(request);
        taskTagMappingService.updateTagMapping(request.getId(), request.getTagList());
        taskGroupMappingService.updateGroupMapping(request.getId(), request.getGroupList());
        return true;
    }

    @Override
    public TaskPluginConfigResponse getPluginConfigByTaskId(Long taskId) {
        TaskEntity taskEntity = taskService.getEntityById(taskId);
        Map<String, String> configMap = configMetadataResolver.convertConfigToMap(taskEntity.getPluginConfig());
        List<PluginConfigFieldWithValueResponse> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId()).stream().map(pluginConfigField -> {

                PluginConfigFieldWithValueResponse response = PluginConfigFieldWithValueResponse.builder().build();
                BeanUtils.copyProperties(pluginConfigField, response);
                response.setValue(configMetadataResolver.decryptToFrontendValue(
                    configMetadataConverter.fromConfigField(pluginConfigField), configMap));
                return response;
            }).collect(Collectors.toList());
        return TaskPluginConfigResponse.builder()
            .fields(configFieldList)
            .options(pluginSelectOptionService.getResponseByPluginId(taskEntity.getPluginId()))
            .build();
    }

    /**
     * 更新数据库中的插件配置信息 如果任务正在运行中，则通知任务更新其配置.
     */
    @Override
    public boolean updatePluginConfig(TaskPluginConfigRequest request) {
        TaskEntity taskEntity = taskService.getEntityById(request.getId());
        List<ConfigMetadata> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId()).stream()
            .map(configMetadataConverter::fromConfigField).collect(Collectors.toList());
        taskEntity.setPluginConfig(configMetadataResolver
            .checkAndEncrypt(configFieldList, request.getPluginConfig(), taskEntity.getPluginConfig()));
        taskService.updateTaskEntity(taskEntity);
        updateTaskIfIsRunning(request.getId(), TaskStatus.resolve(taskEntity.getStatus()));
        return true;
    }

    /**
     * TODO remove 更新数据库中的任务执行频率信息 如果任务正在运行中，则通知任务更新其执行频率.
     */
    @Override
    public boolean updateSchedule(TaskScheduleRequest request) {
        taskService.updateTaskEntity(TaskEntity.builder()
            .id(request.getId())
            //.scheduleUnit(request.getScheduleUnit())
            //.scheduleInterval(request.getScheduleInterval())
            .build());
        updateTaskIfIsRunning(request.getId(), taskService.getTaskStatusById(request.getId()));
        return true;
    }

    private void updateTaskIfIsRunning(Long taskId, TaskStatus taskStatus) {
        if (Objects.equals(taskStatus, TaskStatus.RUNNING)) {
            TaskOperationRequest operationRequest = getOperationRequest(taskId);
            taskHandler.updateTask(operationRequest);
        }
    }

    /**
     * 检查任务状态， 如果正则运行，则抛出异常 检查配置等信息是否完整.
     */
    @Override
    public boolean startByTaskId(Long taskId) {
        TaskStatus taskStatus = taskService.getTaskStatusById(taskId);
        if (!taskStatus.isRunningStatus()) {
            TaskOperationRequest operationRequest = getOperationRequest(taskId);
            taskHandler.startTask(operationRequest);
            // TODO task status 由事件修改
            taskService.updateTaskEntity(TaskEntity.builder().id(taskId).status(TaskStatus.RUNNING.getValue()).build());
            return true;
        }
        throw new EagleEyeException(TASK_IS_ALREADY_RUNNING);
    }

    /**
     * 检查任务状态 如果是等待或失败状态，则抛出异常.
     */
    @Override
    public boolean stopByTaskId(Long taskId) {
        TaskStatus taskStatus = taskService.getTaskStatusById(taskId);
        if (taskStatus.isRunningStatus()) {
            TaskOperationRequest operationRequest = getOperationRequest(taskId);
            taskHandler.stopTask(operationRequest);
            // TODO task status 由事件修改
            taskService.updateTaskEntity(
                TaskEntity.builder().id(taskId).status(TaskStatus.AWAITING.getValue()).build());
            return true;
        }
        throw new EagleEyeException(TASK_IS_ALREADY_NON_RUNNING);
    }

    /**
     * 如果任务正则运行中，则提示先停止任务 否则直接删除任务.
     */
    @Override
    public boolean removeTask(Long taskId) {
        TaskStatus taskStatus = taskService.getTaskStatusById(taskId);
        if (taskStatus.isRunningStatus()) {
            throw new EagleEyeException(TASK_IS_RUNNING_AND_DELETE_ERROR);
        }
        taskService.deleteTaskById(taskId);
        return true;
    }

    @Override
    public List<InvokeErrorRecordResponse> getErrorRecord(Long taskId) {
        return invokeErrorRecordService.getErrorRecords(taskId);
    }

    /**
     * 根据 任务id、告警级别 获取 告警规则.
     */
    @Override
    public TaskAlertRuleResponse getAlertRule(Long taskId, Integer alarmLevel) {
        TaskEntity taskEntity = taskService.getEntityById(taskId);
        Optional<TaskAlertRuleEntity> alertRuleOptional = taskAlertRuleService
            .getByTaskIdAndAlertLevel(taskId, alarmLevel);
        String alertRule = alertRuleOptional.map(TaskAlertRuleEntity::getAlertRules).orElse(null);
        Map<String, String> ruleMap = configMetadataResolver.convertConfigToMap(alertRule);

        List<PluginConfigRuleWithValueResponse> rules = pluginAlertFieldService
            .getListByPluginId(taskEntity.getPluginId()).stream()
            .map(pluginAlertFieldEntity -> {
                PluginConfigRuleWithValueResponse response = PluginConfigRuleWithValueResponse.builder().build();
                BeanUtils.copyProperties(pluginAlertFieldEntity, response);
                ConfigMetadata configMetadata = configMetadataConverter.fromAlertField(pluginAlertFieldEntity);
                response.setValue(configMetadataResolver.decryptToFrontendValue(configMetadata, ruleMap));
                return response;
            }).collect(Collectors.toList());

        return TaskAlertRuleResponse.builder()
            .alertRules(rules)
            .options(pluginSelectOptionService.getResponseByPluginId(taskEntity.getPluginId()))
            .scheduleInterval(alertRuleOptional.map(TaskAlertRuleEntity::getScheduleInterval).orElse(null))
            .scheduleUnit(alertRuleOptional.map(TaskAlertRuleEntity::getScheduleUnit).orElse(null))
            .build();
    }

    @Override
    public boolean updateAlertRule(TaskAlertRuleRequest request) {
        TaskEntity taskEntity = taskService.getEntityById(request.getTaskId());
        Optional<TaskAlertRuleEntity> alertRuleOptional = taskAlertRuleService
            .getByTaskIdAndAlertLevel(request.getTaskId(), request.getAlarmLevel());
        List<ConfigMetadata> configMetadata = pluginAlertFieldService
            .getListByPluginId(taskEntity.getPluginId()).stream()
            .map(configMetadataConverter::fromAlertField).collect(Collectors.toList());
        String encryptValue = configMetadataResolver.checkAndEncrypt(configMetadata,
            request.getAlertRules(), alertRuleOptional.map(TaskAlertRuleEntity::getAlertRules).orElse(null));
        request.setAlertRules(encryptValue);
        taskAlertRuleService.updateByRequest(request);
        return true;
    }

    private TaskOperationRequest getOperationRequest(Long taskId) {
        TaskEntity task = taskService.getEntityById(taskId);
        PluginEntity plugin = pluginService.getEntityById(task.getPluginId());
        List<ConfigMetadata> configFields = pluginConfigFieldService.getListByPluginId(plugin.getId()).stream()
            .map(configMetadataConverter::fromConfigField).collect(Collectors.toList());
        String config = configMetadataResolver.decryptToString(configFields, task.getPluginConfig());
        return TaskOperationRequest.builder()
            .task(task)
            .plugin(plugin)
            .decryptedConfig(config)
            .build();
    }
}