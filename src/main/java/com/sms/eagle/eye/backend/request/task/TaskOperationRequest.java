package com.sms.eagle.eye.backend.request.task;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskOperationRequest {

    private TaskEntity task;
    private PluginEntity plugin;
    private String decryptedConfig;
    private List<TaskAlertRule> alertRules;
}