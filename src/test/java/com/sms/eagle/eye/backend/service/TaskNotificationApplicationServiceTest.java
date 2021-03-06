package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.convert.ChannelFieldConverter;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.convert.TaskAlertNotificationChannelConverter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskNotificationApplicationServiceTest {

    private final Channel channel = mock(Channel.class);
    private final ConfigMetadataResolver configMetadataResolver = mock(ConfigMetadataResolver.class);
    private final ConfigMetadataConverter configMetadataConverter = mock(ConfigMetadataConverter.class);
    private final TaskAlertNotificationService taskAlertNotificationService = mock(TaskAlertNotificationService.class);
    private final TaskAlertNotificationEntity taskAlertNotificationEntity = mock(TaskAlertNotificationEntity.class);
    private final TaskAlertNotificationResponse taskAlertNotificationResponse = mock(
        TaskAlertNotificationResponse.class);
    private final ChannelFieldWithValueResponse channelFieldWithValueResponse = mock(
        ChannelFieldWithValueResponse.class);
    private final TaskAlertNotificationAddRequest request = mock(TaskAlertNotificationAddRequest.class);
    private final ChannelFieldConverter channelFieldConverter = mock(ChannelFieldConverter.class);
    private final TaskAlertNotificationChannelConverter taskAlertNotificationChannelConverter =
        mock(TaskAlertNotificationChannelConverter.class);
    private final TaskAlertNotificationUpdateRequest updateRequest = mock(TaskAlertNotificationUpdateRequest.class);

    private final TaskNotificationApplicationService taskNotificationApplicationService =
        new TaskNotificationApplicationServiceImpl(channel, configMetadataResolver, configMetadataConverter,
            channelFieldConverter, taskAlertNotificationChannelConverter, taskAlertNotificationService);

    private static final Long ID = 1L;
    private static final Integer INTEGER = 1;
    private static final String VALUE = "VALUE";
    private static final String CONFIG = "CONFIG";
    private static final Integer TOTAL_ELEMENTS = 3;

    /**
     * {@link TaskNotificationApplicationServiceImpl#getAlertNotification(Long, Integer)}
     *
     * ???????????? taskId???alarmLevel ????????????????????????????????? ???????????? ??????????????????.
     *
     */
    @Test
    @DisplayName("Test the getAlertNotification method in the Tag Application Service")
    public void getAlertNotification_test() {
        // ??????????????????
        TaskAlertNotificationEntity entity = TaskAlertNotificationEntity.builder()
                .channelType(INTEGER).channelInput(VALUE).build();
        // mock
        List<TaskAlertNotificationEntity> taskAlertNotifications = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            taskAlertNotifications.add(entity);
        }
        when(taskAlertNotificationService.getByTaskIdAndAlarmLevel(ID, INTEGER)).thenReturn(taskAlertNotifications);
        when(taskAlertNotificationChannelConverter.toResponse(entity)).thenReturn(taskAlertNotificationResponse);
        when(taskAlertNotificationEntity.getChannelInput()).thenReturn(VALUE);
        when(taskAlertNotificationEntity.getChannelType()).thenReturn(INTEGER);
        List<ChannelFieldWithValueResponse> responseList = mock(List.class);
        doNothing().when(taskAlertNotificationResponse).setInput(responseList);
        // ??????
        List<TaskAlertNotificationResponse> result = taskNotificationApplicationService.getAlertNotification(ID, INTEGER);
        // ??????
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
    }

    /**
     * {@link TaskNotificationApplicationServiceImpl#addAlertNotification(TaskAlertNotificationAddRequest)}
     *
     * ???????????? TaskAlertNotificationAddRequest??????????????????
     *
     * <p>??? ??????????????? ????????????????????? ???????????? ????????????.
     */
    @Test
    @DisplayName("Test the addAlertNotification method in the Tag Application Service")
    public void addAlertNotification_test() {
        // mock
        when(request.getChannelType()).thenReturn(INTEGER);
        ChannelFieldResponse response = mock(ChannelFieldResponse.class);
        List<ChannelFieldResponse> list = Arrays.asList(response);
        when(channel.getTaskInputFields(INTEGER)).thenReturn(list);
        when(request.getChannelInput()).thenReturn(VALUE);
        when(configMetadataResolver.checkAndEncrypt(anyList(), any())).thenReturn(VALUE);
        doNothing().when(request).setChannelInput(VALUE);
        doNothing().when(taskAlertNotificationService).addByRequest(request);
        // ??????
        boolean result = taskNotificationApplicationService.addAlertNotification(request);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link TaskNotificationApplicationServiceImpl#updateAlertNotification(TaskAlertNotificationUpdateRequest)}
     *
     * ?????? notificationChannelId ????????????????????????.
     */
    @Test
    @DisplayName("Test the updateAlertNotification method in the Tag Application Service")
    public void updateAlertNotification_test() {
        // mock
        when(updateRequest.getAlertNotificationId()).thenReturn(ID);
        when(taskAlertNotificationService.getEntityById(ID)).thenReturn(taskAlertNotificationEntity);
        when(taskAlertNotificationEntity.getChannelType()).thenReturn(INTEGER);
        List<ChannelFieldResponse> list = mock(List.class);
        when(channel.getTaskInputFields(INTEGER)).thenReturn(list);
        when(configMetadataResolver.checkAndEncrypt(anyList(), any())).thenReturn(VALUE);
        doNothing().when(updateRequest).setChannelInput(VALUE);
        doNothing().when(taskAlertNotificationService).updateByRequest(updateRequest);
        // ??????
        boolean result = taskNotificationApplicationService.updateAlertNotification(updateRequest);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link TaskNotificationApplicationServiceImpl#deleteAlertNotification(Long)}
     *
     * ?????? notificationChannelId ????????????????????????.
     */
    @Test
    @DisplayName("Test the deleteAlertNotification method in the Tag Application Service")
    public void deleteAlertNotification_test() {
        // mock
        doNothing().when(taskAlertNotificationService).delete(ID);
        // ??????
        boolean result = taskNotificationApplicationService.deleteAlertNotification(ID);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link TaskNotificationApplicationServiceImpl#test(NotificationEvent)}
     *
     */
    @Test
    @DisplayName("Test the test method in the Tag Application Service")
    public void test_test() {
        // mock
        NotificationEvent notificationEvent = mock(NotificationEvent.class);
        when(channel.notify(notificationEvent)).thenReturn(Boolean.TRUE);
        taskNotificationApplicationService.test(notificationEvent);
        // ???????????????????????????1???
        verify(channel, times(1)).notify(notificationEvent);
    }
}
