package com.sms.eagle.eye.backend.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationChannelService;
import com.sms.eagle.eye.backend.domain.service.TaskAlertNotificationService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.event.TaskAlertEvent;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class TaskAlertListenerTest {

    private final Channel channel = mock(Channel.class);
    private final TaskService taskService = mock(TaskService.class);
    private final ConfigMetadataConverter converter = mock(ConfigMetadataConverter.class);
    private final ConfigMetadataResolver configMetadataResolver = mock(ConfigMetadataResolver.class);
    private final NotificationChannelService notificationChannelService = mock(NotificationChannelService.class);
    private final TaskAlertNotificationService taskAlertNotificationService = mock(TaskAlertNotificationService.class);

    private final TaskAlertListener taskAlertListener = spy(new TaskAlertListener(channel, taskService,
        converter, configMetadataResolver, notificationChannelService, taskAlertNotificationService));

    private static final MockedStatic<AlarmLevel> ALARM_LEVEL_MOCKED_STATIC
        = mockStatic(AlarmLevel.class);

    @AfterAll
    public static void close() {
        ALARM_LEVEL_MOCKED_STATIC.close();
    }

    /**
     * {@link TaskAlertListener#invokeTaskAlert(TaskAlertEvent)}
     */
    @Test
    void invokeTaskAlert_test_1() {
        // mock
        TaskAlertEvent taskAlertEvent = mock(TaskAlertEvent.class);
        // mock taskAlertEvent.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(taskAlertEvent.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskAlertEvent.getAlarmLevel()
        Integer alarmLevel = 1;
        when(taskAlertEvent.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskService.updateTaskEntity()
        doNothing().when(taskService).updateTaskEntity(any(TaskEntity.class));
        // mock static  AlarmLevel.resolve
        AlarmLevel alarmLevelEnum = mock(AlarmLevel.class);
        ALARM_LEVEL_MOCKED_STATIC.when(() -> AlarmLevel.resolve(alarmLevel)).thenReturn(alarmLevelEnum);
        // mock alarmLevelEnum.getName()
        String alarmLevelName = "alarmLevelName";
        when(alarmLevelEnum.getName()).thenReturn(alarmLevelName);
        // taskAlertNotificationService.getByTaskIdAndAlarmLevel()
        TaskAlertNotificationEntity taskAlertNotification = mock(TaskAlertNotificationEntity.class);
        List<TaskAlertNotificationEntity> taskAlertNotificationEntities =
            new ArrayList<>(Collections.singletonList(taskAlertNotification));
        when(taskAlertNotificationService.getByTaskIdAndAlarmLevel(taskId, alarmLevel))
            .thenReturn(taskAlertNotificationEntities);
        // mock taskAlertNotification.getNotificationChannelId()
        Long notificationChannelId = 1L;
        when(taskAlertNotification.getNotificationChannelId()).thenReturn(notificationChannelId);
        // mock notificationChannelService.getEntityById()
        NotificationChannelEntity notificationChannelEntity = mock(NotificationChannelEntity.class);
        when(notificationChannelService.getEntityById(notificationChannelId)).thenReturn(notificationChannelEntity);
        // mock notificationChannelEntity.getConfig()
        String rawChannelConfig = "rawChannelConfig";
        when(notificationChannelEntity.getConfig()).thenReturn(rawChannelConfig);
        // mock taskAlertNotification.getChannelType()
        Integer channelType = 5;
        when(taskAlertNotification.getChannelType()).thenReturn(channelType);
        // mock channel.getConfigFields()
        ChannelFieldResponse configField = mock(ChannelFieldResponse.class);
        List<ChannelFieldResponse> configFields = Collections.singletonList(configField);
        when(channel.getConfigFields(channelType)).thenReturn(configFields);
        // mock converter.fromChannelField()
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        when(converter.fromChannelField(configField)).thenReturn(configMetadata);
        // mock configMetadataResolver.decryptToString()
        String channelConfig = "channelConfig";
        when(configMetadataResolver.decryptToString(Collections.singletonList(configMetadata), rawChannelConfig))
            .thenReturn(channelConfig);
        // mock channel.getTaskInputFields()
        ChannelFieldResponse taskInputFields = mock(ChannelFieldResponse.class);
        when(channel.getTaskInputFields(channelType)).thenReturn(Collections.singletonList(taskInputFields));
        // mock converter.fromChannelField()
        ConfigMetadata inputMetadata = mock(ConfigMetadata.class);
        when(converter.fromChannelField(taskInputFields)).thenReturn(inputMetadata);
        // mock taskAlertNotification.getChannelInput()
        String rawChannelInput = "rawChannelInput";
        when(taskAlertNotification.getChannelInput()).thenReturn(rawChannelInput);
        // mock configMetadataResolver.decryptToString()
        String channelInput = "channelInput";
        when(configMetadataResolver.decryptToString(Collections.singletonList(inputMetadata), rawChannelInput))
            .thenReturn(channelInput);
        // mock channel.notify()
        doReturn(true).when(channel).notify(any(NotificationEvent.class));
        // invoke
        taskAlertListener.invokeTaskAlert(taskAlertEvent);
        // verify
        verify(channel).notify(any(NotificationEvent.class));
    }
}