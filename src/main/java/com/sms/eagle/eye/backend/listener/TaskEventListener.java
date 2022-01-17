package com.sms.eagle.eye.backend.listener;

import com.sms.eagle.eye.backend.event.TaskConfigUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    @EventListener
    public void updateTask(TaskConfigUpdateEvent event) {

    }
}