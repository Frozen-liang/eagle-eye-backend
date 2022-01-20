package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_ALREADY_NON_RUNNING;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_ALREADY_RUNNING;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_RUNNING_AND_DELETE_ERROR;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_NAME_HAS_ALREADY_EXIST;
import static com.sms.eagle.eye.backend.handler.impl.TaskHandlerProxy.TASK_HANDLER_PROXY;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.TaskTagMappingService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.request.task.TaskScheduleRequest;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TaskApplicationServiceImpl implements TaskApplicationService {

    private final TaskHandler taskHandler;
    private final PluginConfigResolver pluginConfigResolver;
    private final TaskService taskService;
    private final PluginService pluginService;
    private final PluginConfigFieldService pluginConfigFieldService;
    private final PluginSelectOptionService pluginSelectOptionService;
    private final TaskTagMappingService taskTagMappingService;
    private final TaskGroupMappingService taskGroupMappingService;
    private final InvokeErrorRecordService invokeErrorRecordService;
    private final ThirdPartyMappingService thirdPartyMappingService;

    public TaskApplicationServiceImpl(@Qualifier(TASK_HANDLER_PROXY) TaskHandler taskHandler,
        PluginConfigResolver pluginConfigResolver, TaskService taskService,
        PluginService pluginService,
        PluginConfigFieldService pluginConfigFieldService,
        PluginSelectOptionService pluginSelectOptionService,
        TaskTagMappingService taskTagMappingService,
        TaskGroupMappingService taskGroupMappingService,
        InvokeErrorRecordService invokeErrorRecordService,
        ThirdPartyMappingService thirdPartyMappingService) {
        this.taskHandler = taskHandler;
        this.pluginConfigResolver = pluginConfigResolver;
        this.taskService = taskService;
        this.pluginService = pluginService;
        this.pluginConfigFieldService = pluginConfigFieldService;
        this.pluginSelectOptionService = pluginSelectOptionService;
        this.taskTagMappingService = taskTagMappingService;
        this.taskGroupMappingService = taskGroupMappingService;
        this.invokeErrorRecordService = invokeErrorRecordService;
        this.thirdPartyMappingService = thirdPartyMappingService;
    }

    @Override
    public CustomPage<TaskResponse> page(TaskQueryRequest request) {
        IPage<TaskResponse> page = taskService.getPage(request);
        page.convert(taskResponse -> {
            taskResponse.setTagList(taskTagMappingService.getTagListByTaskId(taskResponse.getId()));
            taskResponse.setGroupList(taskGroupMappingService.getGroupListByTaskId(taskResponse.getId()));
            return taskResponse;
        });
        return new CustomPage<>(page);
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
        List<PluginConfigFieldEntity> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId());
        return TaskPluginConfigResponse.builder()
            .fields(pluginConfigResolver.decryptToResponse(configFieldList, taskEntity.getPluginConfig()))
            .options(pluginSelectOptionService.getResponseByPluginId(taskEntity.getPluginId()))
            .build();
    }

    /**
     * 更新数据库中的插件配置信息 如果任务正在运行中，则通知任务更新其配置.
     */
    @Override
    public boolean updatePluginConfig(TaskPluginConfigRequest request) {
        TaskEntity taskEntity = taskService.getEntityById(request.getId());
        List<PluginConfigFieldEntity> configFieldList = pluginConfigFieldService
            .getListByPluginId(taskEntity.getPluginId());
        taskEntity.setPluginConfig(pluginConfigResolver
            .checkAndEncrypt(configFieldList, request.getPluginConfig(), taskEntity.getPluginConfig()));
        taskService.updateTaskEntity(taskEntity);
        updateTaskIfIsRunning(request.getId(), TaskStatus.resolve(taskEntity.getStatus()));
        return true;
    }

    /**
     * 更新数据库中的任务执行频率信息 如果任务正在运行中，则通知任务更新其执行频率.
     */
    @Override
    public boolean updateSchedule(TaskScheduleRequest request) {
        taskService.updateTaskEntity(TaskEntity.builder()
            .id(request.getId())
            .scheduleUnit(request.getScheduleUnit())
            .scheduleInterval(request.getScheduleInterval())
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

    // TODO Refactor
    @Override
    public boolean resolveInvokeResult(LambdaInvokeResult request) {
        if (Objects.equals(request.getSuccess(), Boolean.FALSE)) {
            invokeErrorRecordService.addErrorRecord(request.getTaskId(), request.getErrorMsg());
            taskService.updateTaskEntity(TaskEntity.builder()
                .id(request.getTaskId())
                .status(TaskStatus.ERROR.getValue())
                .build());
        } else {
            if (!Objects.equals(request.getTaskId().toString(), request.getMappingId())) {
                thirdPartyMappingService.addPluginSystemUnionIdMapping(request.getTaskId(), request.getMappingId());
            }
        }
        return true;
    }

    private TaskOperationRequest getOperationRequest(Long taskId) {
        TaskEntity task = taskService.getEntityById(taskId);
        PluginEntity plugin = pluginService.getEntityById(task.getPluginId());
        List<PluginConfigFieldEntity> configFields = pluginConfigFieldService.getListByPluginId(plugin.getId());
        String config = pluginConfigResolver.decryptToString(configFields, task.getPluginConfig());
        return TaskOperationRequest.builder()
            .task(task)
            .plugin(plugin)
            .decryptedConfig(config)
            .build();
    }
}