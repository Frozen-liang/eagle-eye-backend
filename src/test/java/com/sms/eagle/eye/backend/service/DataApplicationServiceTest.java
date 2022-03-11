package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.convert.PluginConverter;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.response.task.AlarmLevelResponse;
import com.sms.eagle.eye.backend.service.impl.DataApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DataApplicationServiceTest {


    private final TagService tagService = mock(TagService.class);
    private final TaskService taskService = mock(TaskService.class);
    private final ThirdPartyMappingService thirdPartyMappingService = mock(ThirdPartyMappingService.class);
    private final PluginConverter pluginConverter = mock(PluginConverter.class);
    private final PluginService pluginService = mock(PluginService.class);
    private final DataApplicationService dataApplicationService =
            new DataApplicationServiceImpl(pluginService, tagService, taskService, thirdPartyMappingService);
    private static final MockedStatic<TaskScheduleUnit> ALERT_UNIQUE_FIELD_MOCKED_STATIC
            = mockStatic(TaskScheduleUnit.class);
    private static final MockedStatic<TaskStatus> TASK_STATUS_MOCKED_STATIC
            = mockStatic(TaskStatus.class);
    private static final MockedStatic<AlarmLevel> ALARM_LEVEL_MOCKED_STATIC
            = mockStatic(AlarmLevel.class);
    private static final MockedStatic<NotificationTemplateType> NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC
            = mockStatic(NotificationTemplateType.class);

    private static final Integer ID = 1;
    private static final String NAME = "name";
    private static final String MAPPINGID = "id";

    static {
        TaskScheduleUnit[] taskScheduleUnitList = new TaskScheduleUnit[2];
        taskScheduleUnitList[0] = TaskScheduleUnit.HOUR;
        taskScheduleUnitList[1] = TaskScheduleUnit.MINUTE;
        ALERT_UNIQUE_FIELD_MOCKED_STATIC.when(TaskScheduleUnit::values).thenReturn(taskScheduleUnitList);
        TaskStatus[] taskStatuses = new TaskStatus[3];
        taskStatuses[0] = TaskStatus.AWAITING;
        taskStatuses[1] = TaskStatus.ERROR;
        taskStatuses[2] = TaskStatus.RUNNING;
        TASK_STATUS_MOCKED_STATIC.when(TaskStatus::values).thenReturn(taskStatuses);
        AlarmLevel[] alarmLevels = new AlarmLevel[4];
        alarmLevels[0] = AlarmLevel.EMERGENCY;
        alarmLevels[1] = AlarmLevel.NORMAL;
        alarmLevels[2] = AlarmLevel.IMPORTANT;
        alarmLevels[3] = AlarmLevel.TIPS;
        ALARM_LEVEL_MOCKED_STATIC.when(AlarmLevel::values).thenReturn(alarmLevels);
        NotificationTemplateType[] notificationTemplateTypes = new NotificationTemplateType[1];
        notificationTemplateTypes[0] = NotificationTemplateType.ALERT;
        NOTIFICATION_TEMPLATE_TYPE_MOCKED_STATIC
                .when(NotificationTemplateType::values).thenReturn(notificationTemplateTypes);
    }

    @Test
    @DisplayName("Test the getPluginList method in the AlertApplication Service")
    public void getPluginList_test() {
        when(pluginService.getList()).thenReturn(new ArrayList<>());
        assertThat(dataApplicationService.getPluginList()).isNotNull();
    }

    @Test
    @DisplayName("Test the getScheduleUnitList method in the AlertApplication Service")
    public void getScheduleUnitList_test() {
        List<IdNameResponse<Integer>> idNameResponses = dataApplicationService.getScheduleUnitList();
        assertThat(idNameResponses).isNotNull();
        assertThat(idNameResponses).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getTagList method in the AlertApplication Service")
    public void getTagList_test() {
        when(tagService.getList()).thenReturn(List.of());
        List<IdNameResponse<Long>> responseList = dataApplicationService.getTagList();
        assertThat(responseList).isNotNull();
    }

    @Test
    @DisplayName("Test the getTaskStatusList method in the AlertApplication Service")
    public void getTaskStatusList_test() {
        List<IdNameResponse<Integer>> idNameResponseList = dataApplicationService.getTaskStatusList();
        assertThat(idNameResponseList).isNotNull();
        assertThat(idNameResponseList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getAlarmLevelResponse method in the AlertApplication Service")
    public void getAlarmLevelResponse_test() {
        List<AlarmLevelResponse> alarmLevelResponse = dataApplicationService.getAlarmLevelResponse();
        assertThat(alarmLevelResponse).isNotEmpty();
        assertThat(alarmLevelResponse).isNotNull();
    }

    @Test
    @DisplayName("Test the getTemplateType method in the AlertApplication Service")
    public void getTemplateType_test() {
        List<IdNameResponse<Integer>> templateType = dataApplicationService.getTemplateType();
        assertThat(templateType).isNotEmpty();
        assertThat(templateType).isNotNull();
    }

    @Test
    @DisplayName("Test the getTemplateType method in the AlertApplication Service")
    public void getTaskByMappingId_test() {
        when(thirdPartyMappingService.getTaskIdByPluginSystemUnionId(MAPPINGID)).thenReturn(Optional.empty());
        assertThat(dataApplicationService.getTaskByMappingId(MAPPINGID)).isNotNull();
        assertThat(dataApplicationService.getTaskByMappingId(MAPPINGID)).isEmpty();
    }

    @Test
    @DisplayName("Test the getTaskIdByTaskName method in the AlertApplication Service")
    public void getTaskIdByTaskName_test() {
        when(taskService.getIdByName(NAME)).thenReturn(Optional.empty());
        assertThat(dataApplicationService.getTaskIdByTaskName(NAME)).isNotNull();
    }
}
