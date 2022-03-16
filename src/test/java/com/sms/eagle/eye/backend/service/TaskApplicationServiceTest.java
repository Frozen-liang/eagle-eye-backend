package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.convert.PluginAlertFieldConverter;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
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
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import com.sms.eagle.eye.backend.request.task.TaskAlertRuleRequest;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.service.impl.TaskApplicationServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskApplicationServiceTest {

    private final TaskHandler taskHandler = mock(TaskHandler.class);
    private final ConfigMetadataConverter configMetadataConverter = mock(ConfigMetadataConverter.class);
    private final ConfigMetadataResolver configMetadataResolver = mock(ConfigMetadataResolver.class);
    private final PluginConfigFieldConverter pluginConfigFieldConverter = mock(PluginConfigFieldConverter.class);
    private final TaskService taskService = mock(TaskService.class);
    private final PluginService pluginService = mock(PluginService.class);
    private final PluginAlertFieldConverter pluginAlertFieldConverter = mock(PluginAlertFieldConverter.class);
    private final InvokeErrorRecordService invokeErrorRecordService = mock(InvokeErrorRecordService.class);
    private final PluginConfigFieldService pluginConfigFieldService = mock(PluginConfigFieldService.class);
    private final PluginAlertFieldService pluginAlertFieldService = mock(PluginAlertFieldService.class);
    private final PluginSelectOptionService pluginSelectOptionService = mock(PluginSelectOptionService.class);
    private final TaskTagMappingService taskTagMappingService = mock(TaskTagMappingService.class);
    private final TaskGroupService taskGroupService = mock(TaskGroupService.class);
    private final TaskGroupMappingService taskGroupMappingService = mock(TaskGroupMappingService.class);
    private final TaskAlertRuleService taskAlertRuleService = mock(TaskAlertRuleService.class);
    private final PluginAlarmLevelMappingService AlarmLevelMappingService = mock(PluginAlarmLevelMappingService.class);
    private final TaskQueryRequest taskQueryRequest = mock(TaskQueryRequest.class);
    private final TaskResponse taskResponse = mock(TaskResponse.class);
    private final TaskBasicInfoRequest taskBasicInfoRequest = mock(TaskBasicInfoRequest.class);
    private final TaskEntity taskEntity = mock(TaskEntity.class);
    private final TaskPluginConfigRequest taskPluginConfigRequest = mock(TaskPluginConfigRequest.class);
    private final TaskAlertRuleRequest taskAlertRuleRequest = mock(TaskAlertRuleRequest.class);

    private final TaskApplicationService taskApplicationService =
            new TaskApplicationServiceImpl(taskHandler, configMetadataConverter, pluginConfigFieldConverter,
                pluginAlertFieldConverter, configMetadataResolver, taskService,
                    pluginService, invokeErrorRecordService, pluginConfigFieldService, pluginAlertFieldService,
                    pluginSelectOptionService, taskTagMappingService, taskGroupService, taskGroupMappingService,
                    taskAlertRuleService, AlarmLevelMappingService);

    private static final Long ID = 1L;
    private static final String CONFIG = "CONFIG";
    private static final String STRING = "STRING";
    private static final Integer INTEGER = 1;

    @Test
    @DisplayName("Test the page method in the Task Application Service")
    public void page_test() {
        List<Long> selfAndChildGroupList = getSelfAndChildGroupList(taskQueryRequest);
        when(taskService.getPage(taskQueryRequest, selfAndChildGroupList)).thenReturn(new Page<>());
        when(taskTagMappingService.getTagListByTaskId(ID)).thenReturn(Collections.emptyList());
        when(taskGroupMappingService.getGroupListByTaskId(ID)).thenReturn(Collections.emptyList());
        doNothing().when(taskResponse).setTagList(anyList());
        doNothing().when(taskResponse).setGroupList(anyList());
        assertThat(taskApplicationService.page(taskQueryRequest)).isNotNull();
    }

    private List<Long> getSelfAndChildGroupList(TaskQueryRequest request) {
        if (Objects.nonNull(request.getGroupId())) {
            List<Long> groups = taskGroupService.getChildGroupById(request.getGroupId());
            groups.add(request.getGroupId());
            return groups;
        }
        return Collections.emptyList();
    }

    @Test
    @DisplayName("Test the addTask method in the Tag Application Service")
    public void addTask_test() {
        when(taskService.saveFromRequest(taskBasicInfoRequest)).thenReturn(ID);
        doNothing().when(taskTagMappingService).updateTagMapping(any(), anyList());
        doNothing().when(taskGroupMappingService).updateGroupMapping(any(), anyList());
        assertThat(taskApplicationService.addTask(taskBasicInfoRequest)).isNotNull();
    }

    @Test
    @DisplayName("Test the updateTask method in the Tag Application Service")
    public void updateTask_test() {
        doNothing().when(taskService).updateFromRequest(taskBasicInfoRequest);
        doNothing().when(taskTagMappingService).updateTagMapping(any(),anyList());
        doNothing().when(taskGroupMappingService).updateGroupMapping(any(),anyList());
        assertThat(taskApplicationService.updateTask(taskBasicInfoRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the getPluginConfigByTaskId method in the Tag Application Service")
    public void getPluginConfigByTaskId_test() {
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(configMetadataResolver.convertConfigToMap(CONFIG)).thenReturn(Map.ofEntries());
        when(pluginConfigFieldService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(pluginSelectOptionService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        assertThat(taskApplicationService.getPluginConfigByTaskId(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the updatePluginConfig method in the Tag Application Service")
    public void updatePluginConfig_test() {
        when(taskPluginConfigRequest.getId()).thenReturn(ID);
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(taskEntity.getPluginId()).thenReturn(ID);
        when(pluginConfigFieldService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(configMetadataResolver.checkAndEncrypt(anyList(),any())).thenReturn(STRING);
        doNothing().when(taskEntity).setPluginConfig(CONFIG);
        doNothing().when(taskService).updateTaskEntity(taskEntity);
        when(taskEntity.getStatus()).thenReturn(INTEGER);
        TaskStatus resolve = TaskStatus.resolve(INTEGER);
        updateTaskIfIsRunning(ID,resolve);
        assertThat(taskApplicationService.updatePluginConfig(taskPluginConfigRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the updateSchedule method in the Tag Application Service")
    public void updateSchedule_test() {
//        when(taskScheduleRequest.getId()).thenReturn(ID);
//        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.RUNNING);
//        TaskStatus resolve = TaskStatus.resolve(INTEGER);
//        updateTaskIfIsRunning(ID,resolve);
//        assertThat(taskApplicationService.updateSchedule(taskScheduleRequest)).isTrue();
    }

    private void updateTaskIfIsRunning(Long taskId, TaskStatus taskStatus) {
        if (Objects.equals(taskStatus, TaskStatus.RUNNING)) {
            TaskOperationRequest operationRequest = getOperationRequest(taskId);
            taskHandler.updateTask(operationRequest);
        }
    }

    @Test
    @DisplayName("Test the startByTaskId method in the Tag Application Service")
    public void startByTaskId_test() {
//        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
//        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.RUNNING);
//        doNothing().when(taskHandler).stopTask(taskOperationRequest);
//        doNothing().when(taskService).updateTaskEntity(taskEntity);
//        assertThat(taskApplicationService.stopByTaskId(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the startByTaskId_exception method in the Tag Application Service")
    public void startByTaskId_exception_test() {
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.ERROR);
        assertThatThrownBy(()->taskApplicationService.stopByTaskId(ID)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the removeTask method in the Tag Application Service")
    public void removeTask_test() {
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.ERROR);
        doNothing().when(taskService).deleteTaskById(ID);
        assertThat(taskApplicationService.removeTask(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the removeTask_exception method in the Tag Application Service")
    public void removeTask_exception() {
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.RUNNING);
        assertThatThrownBy(()->taskApplicationService.removeTask(ID)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the getErrorRecord method in the Tag Application Service")
    public void getErrorRecord_test() {
        when(invokeErrorRecordService.getErrorRecords(ID)).thenReturn(Collections.emptyList());
        assertThat(taskApplicationService.getErrorRecord(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the getAlertRule method in the Tag Application Service")
    public void getAlertRule_test() {
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(taskAlertRuleService.getByTaskIdAndAlertLevel(ID,INTEGER)).thenReturn(Optional.empty());
        when(configMetadataResolver.convertConfigToMap(CONFIG)).thenReturn(Map.ofEntries());
        when(taskEntity.getPluginId()).thenReturn(ID);
        when(pluginAlertFieldService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(pluginSelectOptionService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        assertThat(taskApplicationService.getAlertRule(ID,INTEGER)).isNotNull();
    }

    @Test
    @DisplayName("Test the updateAlertRule method in the Tag Application Service")
    public void updateAlertRule() {
        when(taskAlertRuleRequest.getTaskId()).thenReturn(ID);
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(taskEntity.getPluginId()).thenReturn(ID);
        when(pluginAlertFieldService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(configMetadataResolver.checkAndEncrypt(anyList(),any())).thenReturn(STRING);
        doNothing().when(taskAlertRuleRequest).setAlertRules(STRING);
        doNothing().when(taskAlertRuleService).updateByRequest(taskAlertRuleRequest);
        assertThat(taskApplicationService.updateAlertRule(taskAlertRuleRequest)).isTrue();
    }

    private TaskOperationRequest getOperationRequest(Long taskId) {
        TaskEntity task = taskService.getEntityById(taskId);
        PluginEntity plugin = pluginService.getEntityById(task.getPluginId());
        List<ConfigMetadata> configFields = pluginConfigFieldService.getListByPluginId(plugin.getId()).stream()
                .map(configMetadataConverter::fromConfigField).collect(Collectors.toList());
        String config = configMetadataResolver.decryptToString(configFields, task.getPluginConfig());

        List<ConfigMetadata> ruleFields = pluginAlertFieldService.getListByPluginId(task.getPluginId()).stream()
                .map(configMetadataConverter::fromAlertField).collect(Collectors.toList());
        List<TaskAlertRuleEntity> taskAlertRuleList = taskAlertRuleService.getByTaskId(taskId);
        List<TaskAlertRule> taskAlertRules = taskAlertRuleList.stream().map(taskAlertRuleEntity -> {
            String alarmLevel = AlarmLevelMappingService.getMappingLevelByPluginIdAndSystemLevel(
                            task.getPluginId(), taskAlertRuleEntity.getAlarmLevel())
                    .orElse(AlarmLevel.resolve(taskAlertRuleEntity.getAlarmLevel()).getName());
            return TaskAlertRule.builder()
                    .ruleId(taskAlertRuleEntity.getId())
                    .alarmLevel(alarmLevel)
                    .decryptedAlertRule(configMetadataResolver
                            .decryptToString(ruleFields, taskAlertRuleEntity.getAlertRules()))
                    .scheduleInterval(taskAlertRuleEntity.getScheduleInterval())
                    .scheduleUnit(taskAlertRuleEntity.getScheduleUnit())
                    .build();
        }).collect(Collectors.toList());

        return TaskOperationRequest.builder()
                .task(task)
                .plugin(plugin)
                .decryptedConfig(config)
                .alertRules(taskAlertRules)
                .build();
    }
}
