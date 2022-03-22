package com.sms.eagle.eye.backend.listener;

import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.event.DisablePluginEvent;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PluginEventListener {

    private final TaskService taskService;
    private final TaskApplicationService taskApplicationService;

    public PluginEventListener(TaskService taskService,
        TaskApplicationService taskApplicationService) {
        this.taskService = taskService;
        this.taskApplicationService = taskApplicationService;
    }

    @EventListener
    public void disablePlugin(DisablePluginEvent event) {
        List<Long> runningTaskList = taskService.getRunningTaskListByPlugin(event.getPluginId());
        for (Long taskId : runningTaskList) {
            taskApplicationService.stopByTaskId(taskId);
        }
    }
}