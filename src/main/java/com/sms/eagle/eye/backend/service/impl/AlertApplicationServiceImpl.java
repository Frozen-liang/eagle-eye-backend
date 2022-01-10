package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.AlertUniqueField;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.AlertService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertApplicationServiceImpl implements AlertApplicationService {

    private final AlertService alertService;
    private final DataApplicationService dataApplicationService;
    private final TaskService taskService;

    public AlertApplicationServiceImpl(AlertService alertService,
        DataApplicationService dataApplicationService, TaskService taskService) {
        this.alertService = alertService;
        this.dataApplicationService = dataApplicationService;
        this.taskService = taskService;
    }

    @Override
    public CustomPage<AlertResponse> page(AlertQueryRequest request) {
        return new CustomPage<>(alertService.getPage(request));
    }

    @Override
    public boolean resolveWebHook(WebHookRequest request) {
        Optional<Long> taskIdOptional = AlertUniqueField.resolve(request.getUniqueField().toUpperCase(Locale.ROOT))
            .getGetTaskId().apply(dataApplicationService, request.getUniqueValue());
        taskIdOptional.ifPresent(taskId -> {
            TaskEntity task = taskService.getEntityById(taskId);
            alertService.saveAlert(task, request);
        });
        return true;
    }
}