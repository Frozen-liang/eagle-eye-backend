package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskAlertNotificationChannelConverterTest {

    private final TaskAlertNotificationChannelConverter converter = new TaskAlertNotificationChannelConverterImpl();
    private final TaskAlertNotificationEntity notificationEntity = mock(TaskAlertNotificationEntity.class);

    private final Long ID = 1L;
    private final String KEY = "KEY";
    private final Object VAULE = Object.class;

    @Test
    void toResponse_test1() {
        assertThat(converter.toResponse(null)).isNull();
    }

    @Test
    void toResponse_test2() {
        // mock
        when(notificationEntity.getId()).thenReturn(ID);
        TaskAlertNotificationResponse response = converter.toResponse(notificationEntity);
        assertThat(response.getAlertNotificationId()).isEqualTo(ID);
    }


}
