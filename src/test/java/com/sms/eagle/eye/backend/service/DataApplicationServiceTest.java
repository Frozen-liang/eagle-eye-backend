package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.domain.service.TaskAlertRuleService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.model.TaskAlarmInfo;
import com.sms.eagle.eye.backend.response.task.AlarmLevelResponse;
import com.sms.eagle.eye.backend.service.impl.DataApplicationServiceImpl;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class DataApplicationServiceTest {

    private final PluginService pluginService = mock(PluginService.class);
    private final TagService tagService = mock(TagService.class);
    private final TaskService taskService = mock(TaskService.class);
    private final ThirdPartyMappingService thirdPartyMappingService = mock(ThirdPartyMappingService.class);
    private final TaskAlertRuleService taskAlertRuleService = mock(TaskAlertRuleService.class);
    private final PluginAlarmLevelMappingService pluginAlarmLevelMappingService = mock(
        PluginAlarmLevelMappingService.class);

    private final DataApplicationService dataApplicationService =
        spy(new DataApplicationServiceImpl(pluginService, tagService, taskService, thirdPartyMappingService,
            taskAlertRuleService, pluginAlarmLevelMappingService));

    private static final MockedStatic<TaskScheduleUnit> TASK_SCHEDULE_UNIT_MOCKED_STATIC
        = mockStatic(TaskScheduleUnit.class);
    private static final MockedStatic<TaskStatus> TASK_STATUS_MOCKED_STATIC
        = mockStatic(TaskStatus.class);
    private static final MockedStatic<AlarmLevel> ALARM_LEVEL_MOCKED_STATIC
        = mockStatic(AlarmLevel.class);
    private static final MockedStatic<NotificationTemplateType> NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC
        = mockStatic(NotificationTemplateType.class);

    @AfterAll
    public static void close() {
        TASK_SCHEDULE_UNIT_MOCKED_STATIC.close();
        TASK_STATUS_MOCKED_STATIC.close();
        ALARM_LEVEL_MOCKED_STATIC.close();
        NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC.close();
    }

    /**
     * {@link DataApplicationServiceImpl#getPluginList}
     *
     * <p> ??????????????????
     */
    @Test
    @DisplayName("Test the getPluginList method in the AlertApplication Service")
    public void getPluginList_test() {
        // mock
        List<IdNameResponse<Long>> list = mock(List.class);
        when(pluginService.getList()).thenReturn(list);
        // ???????????????
        assertThat(dataApplicationService.getPluginList()).isEqualTo(list);
    }

    /**
     * {@link DataApplicationServiceImpl#getScheduleUnitList()}
     *
     * <p> ??????????????????????????????
     */
    @Test
    @DisplayName("Test the getScheduleUnitList method in the AlertApplication Service")
    public void getScheduleUnitList_test() {
        // mock TaskScheduleUnit
        Integer id = 1;
        String name = "name";
        TaskScheduleUnit taskScheduleUnit = mock(TaskScheduleUnit.class);
        when(taskScheduleUnit.getId()).thenReturn(id);
        when(taskScheduleUnit.getName()).thenReturn(name);
        // mock????????????
        TASK_SCHEDULE_UNIT_MOCKED_STATIC.when(TaskScheduleUnit::values)
            .thenReturn(new TaskScheduleUnit[]{taskScheduleUnit});
        // ??????
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getScheduleUnitList();
        // ??????
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), id))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTagList()}
     *
     * <p> ??????????????????
     */
    @Test
    @DisplayName("Test the getTagList method in the AlertApplication Service")
    public void getTagList_test() {
        // mock
        List<IdNameResponse<Long>> list = mock(List.class);
        when(tagService.getList()).thenReturn(list);
        // ??????
        List<IdNameResponse<Long>> result = dataApplicationService.getTagList();
        // ??????
        assertThat(result).isEqualTo(list);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskStatusList()}
     *
     * <p> ????????????????????????
     */
    @Test
    @DisplayName("Test the getTaskStatusList method in the AlertApplication Service")
    public void getTaskStatusList_test() {
        // mock TaskStatus
        Integer value = 1;
        String name = "name";
        TaskStatus taskStatus = mock(TaskStatus.class);
        when(taskStatus.getValue()).thenReturn(value);
        when(taskStatus.getName()).thenReturn(name);
        // mock????????????
        TASK_STATUS_MOCKED_STATIC.when(TaskStatus::values).thenReturn(new TaskStatus[]{taskStatus});
        // ??????
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getTaskStatusList();
        // ??????
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getAlarmLevelResponse()}
     *
     * <p> ????????????????????????
     */
    @Test
    @DisplayName("Test the getAlarmLevelResponse method in the AlertApplication Service")
    public void getAlarmLevelResponse_test() {
        // mock AlarmLevel
        String name = "name";
        Integer value = 1;
        AlarmLevel alarmLevel = mock(AlarmLevel.class);
        when(alarmLevel.getName()).thenReturn(name);
        when(alarmLevel.getValue()).thenReturn(value);
        // mock????????????
        ALARM_LEVEL_MOCKED_STATIC.when(AlarmLevel::values).thenReturn(new AlarmLevel[]{alarmLevel});
        // ??????
        List<AlarmLevelResponse> list = dataApplicationService.getAlarmLevelResponse();
        // ??????
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list)
            .allMatch(response -> Objects.equals(response.getValue(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTemplateType()}
     *
     * <p> ????????????????????????
     */
    @Test
    @DisplayName("Test the getTemplateType method in the AlertApplication Service")
    public void getTemplateType_test() {
        // mock NotificationTemplateType
        Integer value = 1;
        String name = "name";
        NotificationTemplateType notificationTemplateType = mock(NotificationTemplateType.class);
        when(notificationTemplateType.getValue()).thenReturn(value);
        when(notificationTemplateType.getName()).thenReturn(name);
        // mock????????????
        NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC.when(NotificationTemplateType::values)
            .thenReturn(new NotificationTemplateType[]{notificationTemplateType});
        // ??????
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getTemplateType();
        // ??????
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> ??????1???uniqueValue ????????????????????? mappingId???
     * ?????? mappingId ?????? {@link TaskAlarmInfo#getTaskId()}???
     * ?????????task??? pluginId ??? mappingLevel ?????? {@link TaskAlarmInfo#getAlarmLevel()}
     */
    @Test
    public void getTaskByMappingId_test_1() {
        // param
        String uniqueValue = "mappingId";
        String mappingLevel = "mappingLevel";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() ??????
        Long taskId = 1L;
        doReturn(Optional.of(taskId)).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskService.getEntityById() ??????
        Long pluginId = 2L;
        TaskEntity task = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(task).when(taskService).getEntityById(taskId);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() ??????
        Integer systemLevel = 1;
        doReturn(Optional.of(systemLevel)).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(systemLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> ??????2???uniqueValue ????????????????????? mappingId???
     * ?????? mappingId ?????? {@link TaskAlarmInfo#getTaskId()}???
     * ?????????task??? pluginId ??? mappingLevel ????????? {@link TaskAlarmInfo#getAlarmLevel()}
     */
    @Test
    public void getTaskByMappingId_test_2() {
        // param
        String uniqueValue = "mappingId";
        String mappingLevel = "mappingLevel";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() ??????
        Long taskId = 1L;
        doReturn(Optional.of(taskId)).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskService.getEntityById() ??????
        Long pluginId = 2L;
        TaskEntity task = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(task).when(taskService).getEntityById(taskId);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() ??????
        doReturn(Optional.empty()).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> ??????3???uniqueValue ??? taskAlertRuleId
     */
    @Test
    public void getTaskByMappingId_test_3() {
        // param
        String uniqueValue = "100000";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() ??????
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() ??????
        Long taskId = 1L;
        Integer alarmLevel = 1;
        TaskAlertRuleEntity taskAlertRule = TaskAlertRuleEntity.builder().taskId(taskId).alarmLevel(alarmLevel).build();
        doReturn(Optional.of(taskAlertRule)).when(taskAlertRuleService)
            .getByTaskAlertRuleId(Long.parseLong(uniqueValue));
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(alarmLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> ??????4???uniqueValue ?????? mappingId ??? taskAlertRuleId???????????????????????????
     */
    @Test
    public void getTaskByMappingId_test_4() {
        // param
        String uniqueValue = "100000";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() ??????
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() ??????
        doReturn(Optional.empty()).when(taskAlertRuleService).getByTaskAlertRuleId(Long.parseLong(uniqueValue));
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> ??????5???uniqueValue ?????? mappingId ??? taskAlertRuleId??????????????????????????????
     */
    @Test
    public void getTaskByMappingId_test_5() {
        // param
        String uniqueValue = "randomString";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() ??????
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() ??????
        doReturn(Optional.empty()).when(taskAlertRuleService).getByTaskAlertRuleId(null);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> ??????1???uniqueValue ??????????????????????????? mappingLevel ??????????????? systemLevel
     */
    @Test
    public void getTaskIdByTaskName_test_1() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() ??????
        Long taskId = 1L;
        Long pluginId = 2L;
        TaskEntity taskEntity = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(Optional.of(taskEntity)).when(taskService).getEntityByName(uniqueValue);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() ??????
        Integer systemLevel = 3;
        doReturn(Optional.of(systemLevel)).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(systemLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> ??????2???uniqueValue ???????????????????????? mappingLevel ?????????????????? systemLevel
     */
    @Test
    public void getTaskIdByTaskName_test_2() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() ??????
        Long taskId = 1L;
        Long pluginId = 2L;
        TaskEntity taskEntity = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(Optional.of(taskEntity)).when(taskService).getEntityByName(uniqueValue);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() ??????
        doReturn(Optional.empty()).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> ??????3???uniqueValue ??????????????????
     */
    @Test
    public void getTaskIdByTaskName_test_3() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() ??????
        doReturn(Optional.empty()).when(taskService).getEntityByName(uniqueValue);
        // ??????
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }
}
