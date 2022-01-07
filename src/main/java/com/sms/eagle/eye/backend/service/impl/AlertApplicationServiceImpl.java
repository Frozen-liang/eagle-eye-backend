package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.AlertService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertApplicationServiceImpl implements AlertApplicationService {

    private final AlertService alertService;
    private final ThirdPartyMappingService thirdPartyMappingService;
    private final TaskService taskService;

    public AlertApplicationServiceImpl(AlertService alertService,
        ThirdPartyMappingService thirdPartyMappingService,
        TaskService taskService) {
        this.alertService = alertService;
        this.thirdPartyMappingService = thirdPartyMappingService;
        this.taskService = taskService;
    }

    @Override
    public CustomPage<AlertResponse> page(AlertQueryRequest request) {
        return new CustomPage<>(alertService.getPage(request));
    }

    @Override
    public boolean resolveWebHook(WebHookRequest request) {
        Optional<Long> taskIdOptional = thirdPartyMappingService.getTaskIdByPluginSystemUnionId(request.getMappingId());
        taskIdOptional.ifPresent(taskId -> {
            TaskEntity task = taskService.getEntityById(taskId);
            alertService.saveAlert(task, request);
        });
        return true;
    }
}