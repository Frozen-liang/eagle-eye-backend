package com.sms.eagle.eye.backend.event;

import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusEvent {

    private Long taskId;
    private TaskStatus status;
}