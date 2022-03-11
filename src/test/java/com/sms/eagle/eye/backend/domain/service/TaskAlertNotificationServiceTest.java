package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.service.impl.TaskAlertNotificationServiceImpl;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskAlertNotificationServiceTest {

    private final TaskAlertNotificationService taskAlertNotificationService =
            spy(new TaskAlertNotificationServiceImpl());
    private final TaskAlertNotificationEntity entity = TaskAlertNotificationEntity.builder().build();
    private final TaskAlertNotificationAddRequest request = mock(TaskAlertNotificationAddRequest.class);
    private final TaskAlertNotificationUpdateRequest updateRequest = mock(TaskAlertNotificationUpdateRequest.class);

    private static final String VALUE = "VALUE";
    private static final Long ID = 1L;
    private static final Integer LEVEL = 1;

    private static final MockedStatic<BeanUtils> BEAN_UTILS_MOCKED_STATIC;

    static {
        BEAN_UTILS_MOCKED_STATIC = mockStatic(BeanUtils.class);
    }

    @AfterAll
    private static void close() {
        BEAN_UTILS_MOCKED_STATIC.close();
    }

    @BeforeAll
    private static void initTableInfoForClassList() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""),
                TaskAlertNotificationEntity.class);
    }

    @Test
    @DisplayName("Test the getEntityById method in the Tag TaskAlert Notification Service")
    public void getEntityById_test() {
        TaskAlertNotificationEntity entity = mock(TaskAlertNotificationEntity.class);
        doReturn(entity).when(taskAlertNotificationService).getById(ID);
        assertThat(taskAlertNotificationService.getEntityById(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the getByTaskIdAndAlarmLevel method in the Tag TaskAlert Notification Service")
    public void getByTaskIdAndAlarmLevel_test() {
        List<TaskAlertNotificationEntity> list = mock(List.class);
        doReturn(list).when(taskAlertNotificationService).list(any());
        assertThat(taskAlertNotificationService.getByTaskIdAndAlarmLevel(ID, LEVEL)).isEqualTo(list);
    }

    @Test
    @DisplayName("Test the addByRequest method in the Tag TaskAlert Notification Service")
    public void addByRequest_test() {
        doReturn(true).when(taskAlertNotificationService).save(entity);
        taskAlertNotificationService.addByRequest(request);
        verify(taskAlertNotificationService).save(entity);
    }

    @Test
    @DisplayName("Test the updateByRequest method in the Tag TaskAlert Notification Service")
    public void updateByRequest_test() {
        doReturn(true).when(taskAlertNotificationService).update(any(Wrapper.class));
        when(updateRequest.getAlertNotificationId()).thenReturn(ID);
        when(updateRequest.getChannelInput()).thenReturn(VALUE);
        when(updateRequest.getContent()).thenReturn(VALUE);
        taskAlertNotificationService.updateByRequest(updateRequest);
        verify(taskAlertNotificationService).update(any());
    }

    @Test
    @DisplayName("Test the delete method in the Tag TaskAlert Notification Service")
    public void delete_test() {
        doReturn(true).when(taskAlertNotificationService).removeById(any(Serializable.class));
        taskAlertNotificationService.delete(ID);
        verify(taskAlertNotificationService).removeById(any(Serializable.class));
    }
}
