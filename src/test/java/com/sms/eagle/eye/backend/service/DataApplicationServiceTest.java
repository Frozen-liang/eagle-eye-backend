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
     * <p> 获取插件列表
     */
    @Test
    @DisplayName("Test the getPluginList method in the AlertApplication Service")
    public void getPluginList_test() {
        // mock
        List<IdNameResponse<Long>> list = mock(List.class);
        when(pluginService.getList()).thenReturn(list);
        // 执行、验证
        assertThat(dataApplicationService.getPluginList()).isEqualTo(list);
    }

    /**
     * {@link DataApplicationServiceImpl#getScheduleUnitList()}
     *
     * <p> 获取任务执行频次单位
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
        // mock静态方法
        TASK_SCHEDULE_UNIT_MOCKED_STATIC.when(TaskScheduleUnit::values)
            .thenReturn(new TaskScheduleUnit[]{taskScheduleUnit});
        // 执行
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getScheduleUnitList();
        // 验证
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), id))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTagList()}
     *
     * <p> 获取标签列表
     */
    @Test
    @DisplayName("Test the getTagList method in the AlertApplication Service")
    public void getTagList_test() {
        // mock
        List<IdNameResponse<Long>> list = mock(List.class);
        when(tagService.getList()).thenReturn(list);
        // 执行
        List<IdNameResponse<Long>> result = dataApplicationService.getTagList();
        // 验证
        assertThat(result).isEqualTo(list);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskStatusList()}
     *
     * <p> 获取任务状态列表
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
        // mock静态方法
        TASK_STATUS_MOCKED_STATIC.when(TaskStatus::values).thenReturn(new TaskStatus[]{taskStatus});
        // 执行
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getTaskStatusList();
        // 验证
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getAlarmLevelResponse()}
     *
     * <p> 获取告警级别列表
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
        // mock静态方法
        ALARM_LEVEL_MOCKED_STATIC.when(AlarmLevel::values).thenReturn(new AlarmLevel[]{alarmLevel});
        // 执行
        List<AlarmLevelResponse> list = dataApplicationService.getAlarmLevelResponse();
        // 验证
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list)
            .allMatch(response -> Objects.equals(response.getValue(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTemplateType()}
     *
     * <p> 获取消息模版类型
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
        // mock静态方法
        NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC.when(NotificationTemplateType::values)
            .thenReturn(new NotificationTemplateType[]{notificationTemplateType});
        // 执行
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getTemplateType();
        // 验证
        assertThat(idNameResponses).isNotEmpty();
        assertThat(idNameResponses.size()).isEqualTo(1);
        assertThat(idNameResponses)
            .allMatch(response -> Objects.equals(response.getId(), value))
            .allMatch(response -> Objects.equals(response.getName(), name));
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> 情形1：uniqueValue 是与任务相关的 mappingId，
     * 根据 mappingId 找到 {@link TaskAlarmInfo#getTaskId()}，
     * 再根据task的 pluginId 和 mappingLevel 找到 {@link TaskAlarmInfo#getAlarmLevel()}
     */
    @Test
    public void getTaskByMappingId_test_1() {
        // param
        String uniqueValue = "mappingId";
        String mappingLevel = "mappingLevel";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() 方法
        Long taskId = 1L;
        doReturn(Optional.of(taskId)).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskService.getEntityById() 方法
        Long pluginId = 2L;
        TaskEntity task = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(task).when(taskService).getEntityById(taskId);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() 方法
        Integer systemLevel = 1;
        doReturn(Optional.of(systemLevel)).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(systemLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> 情形2：uniqueValue 是与任务相关的 mappingId，
     * 根据 mappingId 找到 {@link TaskAlarmInfo#getTaskId()}，
     * 但根据task的 pluginId 和 mappingLevel 没找到 {@link TaskAlarmInfo#getAlarmLevel()}
     */
    @Test
    public void getTaskByMappingId_test_2() {
        // param
        String uniqueValue = "mappingId";
        String mappingLevel = "mappingLevel";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() 方法
        Long taskId = 1L;
        doReturn(Optional.of(taskId)).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskService.getEntityById() 方法
        Long pluginId = 2L;
        TaskEntity task = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(task).when(taskService).getEntityById(taskId);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() 方法
        doReturn(Optional.empty()).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> 情形3：uniqueValue 是 taskAlertRuleId
     */
    @Test
    public void getTaskByMappingId_test_3() {
        // param
        String uniqueValue = "100000";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() 方法
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() 方法
        Long taskId = 1L;
        Integer alarmLevel = 1;
        TaskAlertRuleEntity taskAlertRule = TaskAlertRuleEntity.builder().taskId(taskId).alarmLevel(alarmLevel).build();
        doReturn(Optional.of(taskAlertRule)).when(taskAlertRuleService)
            .getByTaskAlertRuleId(Long.parseLong(uniqueValue));
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(alarmLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> 情形4：uniqueValue 不是 mappingId 或 taskAlertRuleId，但是是整形字符串
     */
    @Test
    public void getTaskByMappingId_test_4() {
        // param
        String uniqueValue = "100000";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() 方法
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() 方法
        doReturn(Optional.empty()).when(taskAlertRuleService).getByTaskAlertRuleId(Long.parseLong(uniqueValue));
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskByMappingId(String, String)}
     *
     * <p> 情形5：uniqueValue 不是 mappingId 获 taskAlertRuleId，并且不是整形字符串
     */
    @Test
    public void getTaskByMappingId_test_5() {
        // param
        String uniqueValue = "randomString";
        // mock thirdPartyMappingService.getTaskIdByPluginSystemUnionId() 方法
        doReturn(Optional.empty()).when(thirdPartyMappingService).getTaskIdByPluginSystemUnionId(uniqueValue);
        // mock taskAlertRuleService.getByTaskAlertRuleId() 方法
        doReturn(Optional.empty()).when(taskAlertRuleService).getByTaskAlertRuleId(null);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskByMappingId(uniqueValue, null);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> 情形1：uniqueValue 是任务名称，能根据 mappingLevel 找到对应的 systemLevel
     */
    @Test
    public void getTaskIdByTaskName_test_1() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() 方法
        Long taskId = 1L;
        Long pluginId = 2L;
        TaskEntity taskEntity = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(Optional.of(taskEntity)).when(taskService).getEntityByName(uniqueValue);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() 方法
        Integer systemLevel = 3;
        doReturn(Optional.of(systemLevel)).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTaskId()).isEqualTo(taskId);
        assertThat(result.get().getAlarmLevel()).isEqualTo(systemLevel);
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> 情形2：uniqueValue 是任务名称，根据 mappingLevel 找不到对应的 systemLevel
     */
    @Test
    public void getTaskIdByTaskName_test_2() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() 方法
        Long taskId = 1L;
        Long pluginId = 2L;
        TaskEntity taskEntity = TaskEntity.builder().id(taskId).pluginId(pluginId).build();
        doReturn(Optional.of(taskEntity)).when(taskService).getEntityByName(uniqueValue);
        // mock pluginAlarmLevelMappingService.getSystemLevelByPluginIdAndMappingLevel() 方法
        doReturn(Optional.empty()).when(pluginAlarmLevelMappingService)
            .getSystemLevelByPluginIdAndMappingLevel(pluginId, mappingLevel);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link DataApplicationServiceImpl#getTaskIdByTaskName(String, String)}
     *
     * <p> 情形3：uniqueValue 不是任务名称
     */
    @Test
    public void getTaskIdByTaskName_test_3() {
        // param
        String uniqueValue = "taskName";
        String mappingLevel = "mappingLevel";
        // mock taskService.getEntityByName() 方法
        doReturn(Optional.empty()).when(taskService).getEntityByName(uniqueValue);
        // 执行
        Optional<TaskAlarmInfo> result = dataApplicationService.getTaskIdByTaskName(uniqueValue, mappingLevel);
        // Assert
        assertThat(result.isPresent()).isFalse();
    }
}
