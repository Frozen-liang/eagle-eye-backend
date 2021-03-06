package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.Internal;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_ALREADY_NON_RUNNING;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_IS_RUNNING_AND_DELETE_ERROR;
import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_NAME_HAS_ALREADY_EXIST;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.model.TaskAlarmInfo;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.task.TaskAlertRuleResponse;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.service.impl.DataApplicationServiceImpl;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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

import java.util.Arrays;
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


    /**
     * {@link TaskApplicationServiceImpl#page(TaskQueryRequest)}
     * <p>
     * ???????????? TaskQueryRequest?????? ????????????????????????.
     */
    @Test
    @DisplayName("Test the page method in the Task Application Service")
    public void page_test() {
        // mock
        List<Long> selfAndChildGroupList = getSelfAndChildGroupList(taskQueryRequest);
        IPage<TaskResponse> responseIPage = mock(IPage.class);
        when(taskService.getPage(taskQueryRequest, selfAndChildGroupList)).thenReturn(responseIPage);
        List<TaskResponse> responseList = mock(List.class);
        doReturn(responseList).when(responseIPage).getRecords();
        List<Long> longList = mock(List.class);
        when(taskTagMappingService.getTagListByTaskId(ID)).thenReturn(longList);
        when(taskGroupMappingService.getGroupListByTaskId(ID)).thenReturn(longList);
        doNothing().when(taskResponse).setTagList(longList);
        doNothing().when(taskResponse).setGroupList(longList);
        // ??????
        CustomPage<TaskResponse> page = taskApplicationService.page(taskQueryRequest);
        // ??????
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isEqualTo(responseList);
    }

    private List<Long> getSelfAndChildGroupList(TaskQueryRequest request) {
        if (Objects.nonNull(request.getGroupId())) {
            List<Long> groups = taskGroupService.getChildGroupById(request.getGroupId());
            groups.add(request.getGroupId());
            return groups;
        }
        return Collections.emptyList();
    }

    /**
     * {@link TaskApplicationServiceImpl#addTask(TaskBasicInfoRequest)}
     * <p>
     * ???????????? TaskBasicInfoRequest??????????????????.
     *
     * <p> ??????1???TaskName?????????
     */
    @Test
    @DisplayName("Test the addTask method in the Tag Application Service")
    public void addTask_test() {
        // ??????????????????
        Long taskId = 1L;
        // mock
        when(taskService.saveFromRequest(taskBasicInfoRequest)).thenReturn(ID);
        List<Long> list = mock(List.class);
        doNothing().when(taskTagMappingService).updateTagMapping(taskId, list);
        doNothing().when(taskGroupMappingService).updateGroupMapping(taskId, list);
        // ??????
        String result = taskApplicationService.addTask(taskBasicInfoRequest);
        // ??????
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(taskId.toString());
    }

    /**
     * {@link TaskApplicationServiceImpl#addTask(TaskBasicInfoRequest)}
     * <p>
     * ???????????? TaskBasicInfoRequest??????????????????.
     *
     * <p> ??????2???TaskName?????? ????????????
     */
    @Test
    @DisplayName("Test the addTask method in the Tag Application Service")
    public void addTask_test1() {
        // mock
        TaskBasicInfoRequest infoRequest = mock(TaskBasicInfoRequest.class);
        when(infoRequest.getName()).thenReturn(STRING);
        when(taskService.countByName(STRING)).thenReturn(INTEGER);
        // ????????????
        assertThatThrownBy(() -> taskApplicationService.addTask(infoRequest))
                .isInstanceOf(EagleEyeException.class).extracting("code").isEqualTo(TASK_NAME_HAS_ALREADY_EXIST.getCode());
    }

    /**
     * {@link TaskApplicationServiceImpl#updateTask(TaskBasicInfoRequest)}
     *
     * ???????????? TaskBasicInfoRequest????????????????????????.
     *
     * <p>??????1???TaskName??????
     */
    @Test
    @DisplayName("Test the updateTask method in the Tag Application Service")
    public void updateTask_test1() {
        // mock
        TaskBasicInfoRequest infoRequest = mock(TaskBasicInfoRequest.class);
        when(infoRequest.getName()).thenReturn(STRING);
        when(taskService.countByName(STRING)).thenReturn(INTEGER);
        List<Long> list = mock(List.class);
        doNothing().when(taskService).updateFromRequest(infoRequest);
        doNothing().when(taskTagMappingService).updateTagMapping(ID, list);
        doNothing().when(taskGroupMappingService).updateGroupMapping(ID, list);
        // ??????
        boolean updateTask = taskApplicationService.updateTask(infoRequest);
        // ??????
        assertThat(updateTask).isTrue();
    }

    /**
     * {@link TaskApplicationServiceImpl#updateTask(TaskBasicInfoRequest)}
     *
     * ???????????? TaskBasicInfoRequest????????????????????????.
     *
     * <p>??????1???TaskName???????????? ????????????
     */
    @Test
    @DisplayName("Test the updateTask method in the Tag Application Service")
    public void updateTask_exception_test() {
        // ??????????????????
        Integer countByName = 2;
        // mock
        TaskBasicInfoRequest infoRequest = mock(TaskBasicInfoRequest.class);
        when(infoRequest.getName()).thenReturn(STRING);
        when(taskService.countByName(STRING)).thenReturn(countByName);
        // ????????????
        assertThatThrownBy(() -> taskApplicationService.updateTask(infoRequest))
                .isInstanceOf(EagleEyeException.class);
    }

    /**
     * {@link TaskApplicationServiceImpl#getPluginConfigByTaskId(Long)}
     *
     * ???????????? taskId????????????????????????????????????.
     */
    @Test
    @DisplayName("Test the getPluginConfigByTaskId method in the Tag Application Service")
    public void getPluginConfigByTaskId_test() {
        // mock
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        Map<String, Object> map = mock(Map.class);
        when(configMetadataResolver.convertConfigToMap(CONFIG)).thenReturn(map);
        PluginConfigFieldEntity pluginConfigFieldEntity = mock(PluginConfigFieldEntity.class);
        List<PluginConfigFieldEntity> fieldEntityList = Arrays.asList(pluginConfigFieldEntity);
        when(pluginConfigFieldService.getListByPluginId(ID)).thenReturn(fieldEntityList);
        PluginSelectOptionResponse optionResponse = mock(PluginSelectOptionResponse.class);
        List<PluginSelectOptionResponse> responseList = Arrays.asList(optionResponse);
        when(pluginSelectOptionService.getResponseByPluginId(ID)).thenReturn(responseList);
        when(taskEntity.getPluginId()).thenReturn(ID);
        // ??????
        TaskPluginConfigResponse pluginConfigByTaskId = taskApplicationService.getPluginConfigByTaskId(ID);
        // ??????
        assertThat(pluginConfigByTaskId).isNotNull();
        assertThat(pluginConfigByTaskId.getFields().size()).isEqualTo(1);
    }

    /**
     * {@link TaskApplicationServiceImpl#updatePluginConfig(TaskPluginConfigRequest)}
     * 
     * ????????????????????????????????? ????????????????????????????????????????????????????????????.
     */
    @Test
    @DisplayName("Test the updatePluginConfig method in the Tag Application Service")
    public void updatePluginConfig_test() {
        // mock
        when(taskPluginConfigRequest.getId()).thenReturn(ID);
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(taskEntity.getPluginId()).thenReturn(ID);
        List<PluginConfigFieldEntity> list = mock(List.class);
        when(pluginConfigFieldService.getListByPluginId(ID)).thenReturn(list);
        when(taskPluginConfigRequest.getPluginConfig()).thenReturn(STRING);
        List<ConfigMetadata> configFieldList = mock(List.class);
        when(configMetadataResolver.checkAndEncrypt(configFieldList, STRING)).thenReturn(STRING);
        doNothing().when(taskEntity).setPluginConfig(CONFIG);
        doNothing().when(taskService).updateTaskEntity(taskEntity);
        when(taskEntity.getStatus()).thenReturn(INTEGER);
        TaskStatus resolve = TaskStatus.resolve(INTEGER);
        updateTaskIfIsRunning(ID, resolve);
        // ??????
        boolean result = taskApplicationService.updatePluginConfig(taskPluginConfigRequest);
        // ??????
        assertThat(result).isTrue();
    }

    private void updateTaskIfIsRunning(Long taskId, TaskStatus taskStatus) {
        if (Objects.equals(taskStatus, TaskStatus.RUNNING)) {
            TaskOperationRequest operationRequest = getOperationRequest(taskId);
            taskHandler.updateTask(operationRequest);
        }
    }

    /**
     * {@link TaskApplicationServiceImpl#startByTaskId(Long)}
     *
     * ?????????????????? id????????????
     *
     * <p>??????1????????????????????????
     * TODO ?????????????????????????????????.
     */
    @Test
    @DisplayName("Test the startByTaskId method in the Tag Application Service")
    public void startByTaskId_test() {
//        // mock
//        TaskStatus taskStatus = mock(TaskStatus.class);
//        when(taskStatus.isRunningStatus()).thenReturn(false);
//        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
//        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.RUNNING);
//        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
//        doReturn(operationRequest).when(getOperationRequest(taskEntity.getId()));       getOperationRequest(taskEntity.getId())
//        when(operationRequest.getTask()).thenReturn(taskEntity);
//        doNothing().when(taskHandler).stopTask(operationRequest);
//        doNothing().when(taskService).updateTaskEntity(taskEntity);
//        // ??????
//        boolean result = taskApplicationService.stopByTaskId(ID);
//        // ??????
//        assertThat(result).isTrue();
    }

    /**
     * {@link TaskApplicationServiceImpl#startByTaskId(Long)}
     *
     * ?????????????????? id????????????
     *
     * <p>??????2????????????????????????????????????????????????.
     * TODO ?????????????????????????????????.
     */
    @Test
    @DisplayName("Test the startByTaskId_exception method in the Tag Application Service")
    public void startByTaskId_exception_test() {
        // mock
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.ERROR);
        // ????????????
        assertThatThrownBy(() -> taskApplicationService.stopByTaskId(ID)).isInstanceOf(EagleEyeException.class)
                .extracting("code").isEqualTo(TASK_IS_ALREADY_NON_RUNNING.getCode());
    }

    /**
     * {@link TaskApplicationServiceImpl#removeTask(Long)}
     *
     * ??????1????????????????????????????????????taskId ??????????????? ????????????????????????.
     */
    @Test
    @DisplayName("Test the removeTask method in the Tag Application Service")
    public void removeTask_test() {
        // mock
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.ERROR);
        doNothing().when(taskService).deleteTaskById(ID);
        // ??????
        boolean result = taskApplicationService.removeTask(ID);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link TaskApplicationServiceImpl#removeTask(Long)}
     *
     * ??????2???????????????????????????????????????
     */
    @Test
    @DisplayName("Test the removeTask_exception method in the Tag Application Service")
    public void removeTask_exception() {
        // mock
        when(taskService.getTaskStatusById(ID)).thenReturn(TaskStatus.RUNNING);
        // ????????????
        assertThatThrownBy(() -> taskApplicationService.removeTask(ID)).isInstanceOf(EagleEyeException.class)
                .extracting("code").isEqualTo(TASK_IS_RUNNING_AND_DELETE_ERROR.getCode());
    }

    /**
     * {@link TaskApplicationServiceImpl#getErrorRecord(Long)}
     *
     * ???????????? taskId?????????????????????????????????.
     *
     */
    @Test
    @DisplayName("Test the getErrorRecord method in the Tag Application Service")
    public void getErrorRecord_test() {
        // mock
        List<InvokeErrorRecordResponse> responseList= mock(List.class);
        when(invokeErrorRecordService.getErrorRecords(ID)).thenReturn(responseList);
        // ??????
        List<InvokeErrorRecordResponse> result = taskApplicationService.getErrorRecord(ID);
        // ??????
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(responseList);
    }

    /**
     * {@link TaskApplicationServiceImpl#getAlertRule(Long, Integer)}
     *
     * ???????????? taskId???alarmLevel ?????? ????????????.
     */
    @Test
    @DisplayName("Test the getAlertRule method in the Tag Application Service")
    public void getAlertRule_test() {
        // mock
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        Optional<TaskAlertRuleEntity> entityOptional = mock(Optional.class);
        when(taskAlertRuleService.getByTaskIdAndAlertLevel(ID, INTEGER)).thenReturn(entityOptional);
        when(configMetadataResolver.convertConfigToMap(CONFIG)).thenReturn(Map.ofEntries());
        when(taskEntity.getPluginId()).thenReturn(ID);
        PluginAlertFieldEntity entity = mock(PluginAlertFieldEntity.class);
        List<PluginAlertFieldEntity> fieldEntityList = Arrays.asList(entity);
        when(pluginAlertFieldService.getListByPluginId(ID)).thenReturn(fieldEntityList);
        List<PluginSelectOptionResponse> responseList = mock(List.class);
        when(pluginSelectOptionService.getResponseByPluginId(ID)).thenReturn(responseList);
        // ??????
        TaskAlertRuleResponse result = taskApplicationService.getAlertRule(ID, INTEGER);
        // ??????
        assertThat(result).isNotNull();
        assertThat(result.getOptions()).isEqualTo(responseList);
        assertThat(result.getAlertRules().size()).isEqualTo(1);
    }

    /**
     * {@link TaskApplicationServiceImpl#updateTask(TaskBasicInfoRequest)}
     *
     * ???????????? TaskBasicInfoRequest ???????????????????????????.
     *
     */
    @Test
    @DisplayName("Test the updateAlertRule method in the Tag Application Service")
    public void updateAlertRule() {
        // mock
        when(taskAlertRuleRequest.getTaskId()).thenReturn(ID);
        when(taskService.getEntityById(ID)).thenReturn(taskEntity);
        when(taskEntity.getPluginId()).thenReturn(ID);
        List<PluginAlertFieldEntity> fieldEntityList = mock(List.class);
        when(pluginAlertFieldService.getListByPluginId(ID)).thenReturn(fieldEntityList);
        when(configMetadataResolver.checkAndEncrypt(anyList(), any())).thenReturn(STRING);
        doNothing().when(taskAlertRuleRequest).setAlertRules(STRING);
        doNothing().when(taskAlertRuleService).updateByRequest(taskAlertRuleRequest);
        // ??????
        boolean result = taskApplicationService.updateAlertRule(taskAlertRuleRequest);
        // ??????
        assertThat(result).isTrue();
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
