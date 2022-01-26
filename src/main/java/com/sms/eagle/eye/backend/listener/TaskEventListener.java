package com.sms.eagle.eye.backend.listener;

import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.event.TaskConfigUpdateEvent;
import com.sms.eagle.eye.backend.event.TaskInvokeFailedEvent;
import com.sms.eagle.eye.backend.event.TaskInvokeSuccessEvent;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.Objects;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    private final TaskService taskService;
    private final InvokeErrorRecordService invokeErrorRecordService;
    private final ThirdPartyMappingService thirdPartyMappingService;
    private final TaskApplicationService taskApplicationService;

    public TaskEventListener(TaskService taskService,
        InvokeErrorRecordService invokeErrorRecordService,
        ThirdPartyMappingService thirdPartyMappingService,
        TaskApplicationService taskApplicationService) {
        this.taskService = taskService;
        this.invokeErrorRecordService = invokeErrorRecordService;
        this.thirdPartyMappingService = thirdPartyMappingService;
        this.taskApplicationService = taskApplicationService;
    }

    @EventListener
    public void updateTask(TaskConfigUpdateEvent event) {

    }

    @EventListener
    public void invokeSuccess(TaskInvokeSuccessEvent event) {
        if (!Objects.equals(event.getTaskId().toString(), event.getMappingId())) {
            thirdPartyMappingService.addPluginSystemUnionIdMapping(event.getTaskId(), event.getMappingId());
        }
    }

    @EventListener
    public void invokeFailed(TaskInvokeFailedEvent event) {
        invokeErrorRecordService.addErrorRecord(event.getTaskId(), event.getErrMsg());
        taskApplicationService.stopByTaskId(event.getTaskId());
        taskService.updateTaskEntity(TaskEntity.builder()
            .id(event.getTaskId())
            .status(TaskStatus.ERROR.getValue())
            .build());
    }
}