package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.service.TaskAlertNotificationService;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import com.sms.eagle.eye.backend.service.impl.TaskNotificationApplicationServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskNotificationApplicationServiceTest {

    private final Channel channel = mock(Channel.class);
    private final ConfigMetadataResolver configMetadataResolver = mock(ConfigMetadataResolver.class);
    private final ConfigMetadataConverter configMetadataConverter = mock(ConfigMetadataConverter.class);
    private final TaskAlertNotificationService taskAlertNotificationService = mock(TaskAlertNotificationService.class);
    private final TaskAlertNotificationEntity taskAlertNotificationEntity = mock(TaskAlertNotificationEntity.class);
    private final TaskAlertNotificationResponse taskAlertNotificationResponse = mock(TaskAlertNotificationResponse.class);
    private final ChannelFieldWithValueResponse channelFieldWithValueResponse = mock(ChannelFieldWithValueResponse.class);
    private final TaskAlertNotificationAddRequest request = mock(TaskAlertNotificationAddRequest.class);
    private final TaskAlertNotificationUpdateRequest updateRequest = mock(TaskAlertNotificationUpdateRequest.class);

    private final TaskNotificationApplicationService taskNotificationApplicationService =
            new TaskNotificationApplicationServiceImpl(channel, configMetadataResolver, configMetadataConverter,
                    taskAlertNotificationService);

    private static final Long ID = 1L;
    private static final Integer INTEGER = 1;
    private static final String VALUE = "VALUE";
    private static final String CONFIG = "CONFIG";
    private static final Integer TOTAL_ELEMENTS = 10;

    private static final MockedStatic<CollectionUtils> COLLECTION_UTILS_MOCKED_STATIC;

    static {
        COLLECTION_UTILS_MOCKED_STATIC = mockStatic(CollectionUtils.class);
    }

    @AfterAll
    private static void close() {
        COLLECTION_UTILS_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the getAlertNotification method in the Tag Application Service")
    public void getAlertNotification_test() {
        List<TaskAlertNotificationEntity> taskAlertNotifications = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            taskAlertNotifications.add(TaskAlertNotificationEntity.builder().build());
        }
        when(taskAlertNotificationService.getByTaskIdAndAlarmLevel(ID, INTEGER)).thenReturn(taskAlertNotifications);
        when(CollectionUtils.isEmpty(any())).thenReturn(Boolean.TRUE);
        when(taskAlertNotificationEntity.getChannelInput()).thenReturn(VALUE);
        when(configMetadataResolver.convertConfigToMap(CONFIG)).thenReturn(Map.ofEntries());
        when(taskAlertNotificationResponse.getChannelType()).thenReturn(INTEGER);
        when(channel.getTaskInputFields(INTEGER)).thenReturn(Collections.emptyList());
        when(configMetadataResolver.decryptToFrontendValue(any(), any())).thenReturn(Object.class);
        doNothing().when(channelFieldWithValueResponse).setValue(any());
        doNothing().when(taskAlertNotificationResponse).setInput(any());
        assertThat(taskNotificationApplicationService.getAlertNotification(ID, INTEGER)).isNotNull();
    }

    @Test
    @DisplayName("Test the addAlertNotification method in the Tag Application Service")
    public void addAlertNotification_test() {
        when(request.getChannelType()).thenReturn(INTEGER);
        when(channel.getTaskInputFields(INTEGER)).thenReturn(Collections.emptyList());
        when(request.getChannelInput()).thenReturn(VALUE);
        when(configMetadataResolver.checkAndEncrypt(anyList(),any())).thenReturn(VALUE);
        doNothing().when(request).setChannelInput(VALUE);
        doNothing().when(taskAlertNotificationService).addByRequest(request);
        assertThat(taskNotificationApplicationService.addAlertNotification(request)).isTrue();
    }

    @Test
    @DisplayName("Test the updateAlertNotification method in the Tag Application Service")
    public void updateAlertNotification_test() {
        when(updateRequest.getAlertNotificationId()).thenReturn(ID);
        when(taskAlertNotificationService.getEntityById(ID)).thenReturn(taskAlertNotificationEntity);
        when(taskAlertNotificationEntity.getChannelType()).thenReturn(INTEGER);
        when(configMetadataResolver.checkAndEncrypt(anyList(),any())).thenReturn(VALUE);
        doNothing().when(updateRequest).setChannelInput(VALUE);
        doNothing().when(taskAlertNotificationService).updateByRequest(updateRequest);
        assertThat(taskNotificationApplicationService.updateAlertNotification(updateRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteAlertNotification method in the Tag Application Service")
    public void deleteAlertNotification_test() {
        doNothing().when(taskAlertNotificationService).delete(ID);
        assertThat(taskNotificationApplicationService.deleteAlertNotification(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the test method in the Tag Application Service")
    public void test_test() {
        NotificationEvent notificationEvent = mock(NotificationEvent.class);
        when(channel.notify(notificationEvent)).thenReturn(Boolean.TRUE);
        taskNotificationApplicationService.test(notificationEvent);
        verify(channel,times(1)).notify(notificationEvent);
    }
}
