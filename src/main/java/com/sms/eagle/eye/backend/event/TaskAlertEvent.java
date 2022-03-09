package com.sms.eagle.eye.backend.event;

import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertEvent {

    private TaskEntity task;
    private Integer alarmLevel;
    private String alarmMessage;
}