package com.sms.eagle.eye.backend.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.event.TaskInvokeFailedEvent;
import com.sms.eagle.eye.backend.event.TaskInvokeSuccessEvent;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import org.junit.jupiter.api.Test;

public class TaskEventListenerTest {

    private final TaskService taskService = mock(TaskService.class);
    private final InvokeErrorRecordService invokeErrorRecordService = mock(InvokeErrorRecordService.class);
    private final ThirdPartyMappingService thirdPartyMappingService = mock(ThirdPartyMappingService.class);
    private final TaskApplicationService taskApplicationService = mock(TaskApplicationService.class);

    private final TaskEventListener taskEventListener = spy(new TaskEventListener(taskService,
        invokeErrorRecordService, thirdPartyMappingService, taskApplicationService));

    /**
     * {@link TaskEventListener#invokeSuccess(TaskInvokeSuccessEvent)}
     *
     * <p>情形1：返回的 {@link TaskInvokeSuccessEvent#getMappingId()} 与
     * {@link TaskInvokeSuccessEvent#getTaskId()} 不相同
     */
    @Test
    void invokeSuccess_test_1() {
        TaskInvokeSuccessEvent event = mock(TaskInvokeSuccessEvent.class);
        // mock event.getTaskId()
        Long taskId = 1L;
        when(event.getTaskId()).thenReturn(taskId);
        // mock event.getMappingId()
        String mappingId = "mappingId";
        when(event.getMappingId()).thenReturn(mappingId);
        // mock thirdPartyMappingService.addPluginSystemUnionIdMapping()
        doNothing().when(thirdPartyMappingService).addPluginSystemUnionIdMapping(taskId, mappingId);
        // invoke
        taskEventListener.invokeSuccess(event);
        // verify
        verify(thirdPartyMappingService).addPluginSystemUnionIdMapping(taskId, mappingId);
    }

    /**
     * {@link TaskEventListener#invokeFailed(TaskInvokeFailedEvent)}
     */
    @Test
    void invokeFailed_test_1() {
        TaskInvokeFailedEvent event = mock(TaskInvokeFailedEvent.class);
        // mock event.getTaskId()
        Long taskId = 1L;
        when(event.getTaskId()).thenReturn(taskId);
        // mock event.getMappingId()
        String errMsg = "errMsg";
        when(event.getErrMsg()).thenReturn(errMsg);
        // mock invokeErrorRecordService.addErrorRecord
        doNothing().when(invokeErrorRecordService).addErrorRecord(taskId, errMsg);
        // mock taskApplicationService.stopByTaskId
        doReturn(true).when(taskApplicationService).stopByTaskId(taskId);
        // mock taskService.updateTaskEntity
        doNothing().when(taskService).updateTaskEntity(any(TaskEntity.class));
        // invoke
        taskEventListener.invokeFailed(event);
        // verify
        verify(invokeErrorRecordService).addErrorRecord(taskId, errMsg);
        verify(taskApplicationService).stopByTaskId(taskId);
        verify(taskService).updateTaskEntity(any(TaskEntity.class));
    }

    /**
     * {@link TaskEventListener#invokeFailed(TaskInvokeFailedEvent)}
     */
    @Test
    void invokeFailed_test_2() {
        TaskInvokeFailedEvent event = mock(TaskInvokeFailedEvent.class);
        // mock event.getTaskId()
        Long taskId = 1L;
        when(event.getTaskId()).thenReturn(taskId);
        // mock event.getMappingId()
        String errMsg = "errMsg";
        when(event.getErrMsg()).thenReturn(errMsg);
        // mock invokeErrorRecordService.addErrorRecord
        EagleEyeException exception = mock(EagleEyeException.class);
        doThrow(exception).when(invokeErrorRecordService).addErrorRecord(taskId, errMsg);
        // invoke
        taskEventListener.invokeFailed(event);
        // verify
        verify(taskApplicationService, times(0)).stopByTaskId(taskId);
    }
}