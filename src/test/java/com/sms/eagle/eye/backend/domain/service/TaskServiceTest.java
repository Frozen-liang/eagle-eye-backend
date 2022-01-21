package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.convert.TaskConverter;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskMapper;
import com.sms.eagle.eye.backend.domain.service.impl.TaskServiceImpl;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class TaskServiceTest {

    TaskMapper taskMapper = mock(TaskMapper.class);
    TaskConverter taskConverter = mock(TaskConverter.class);
    TaskServiceImpl taskService = spy(new TaskServiceImpl(taskConverter));

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

//    @Test
//    void getPage_test() {
//        IPage<TaskResponse> result = mock(IPage.class);
//        Page<TaskEntity> page = mock(Page.class);
//        TaskQueryRequest request = mock(TaskQueryRequest.class);
//        when(request.getPageInfo()).thenReturn(page);
//        doReturn(taskMapper).when(taskService).getBaseMapper();
//        when(taskMapper.getPage(page, request)).thenReturn(result);
//        assertThat(taskService.getPage(request)).isEqualTo(result);
//    }

    @Test
    void saveFromRequest_test() {
        String username = "username";
        UserInfo userInfo = mock(UserInfo.class);
        when(userInfo.getUsername()).thenReturn(username);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);

        TaskBasicInfoRequest request = mock(TaskBasicInfoRequest.class);
        TaskEntity entity = mock(TaskEntity.class);
        when(taskConverter.toEntity(request)).thenReturn(entity);
        doNothing().when(entity).setCreator(username);
        doReturn(true).when(taskService).save(entity);
        taskService.saveFromRequest(request);
        verify(taskService).save(entity);
    }

    @Test
    void updateFromRequest_test() {
        TaskBasicInfoRequest request = mock(TaskBasicInfoRequest.class);
        TaskEntity entity = mock(TaskEntity.class);
        when(taskConverter.toEntity(request)).thenReturn(entity);
        doReturn(true).when(taskService).updateById(entity);
        taskService.updateFromRequest(request);
        verify(taskService).updateById(entity);
    }

    @Test
    void getEntityById_test() {
        Long id = 1L;
        TaskEntity entity = mock(TaskEntity.class);
        doReturn(entity).when(taskService).getById(id);
        assertThat(taskService.getEntityById(id)).isEqualTo(entity);
    }

    @Test
    void getEntityById_test2() {
        Long id = 1L;
        doReturn(null).when(taskService).getById(id);
        assertThatThrownBy(() -> taskService.getEntityById(id))
            .isInstanceOf(EagleEyeException.class);
    }

    @Test
    void updateTaskEntity_test() {
        TaskEntity task = mock(TaskEntity.class);
        doReturn(Boolean.TRUE).when(taskService).updateById(task);
        taskService.updateTaskEntity(task);
        verify(taskService).updateById(task);
    }

    @Test
    void deleteTaskById_test() {
        Long id = 1L;
        doReturn(Boolean.TRUE).when(taskService).removeById(id);
        taskService.deleteTaskById(id);
        verify(taskService).removeById(id);
    }

}