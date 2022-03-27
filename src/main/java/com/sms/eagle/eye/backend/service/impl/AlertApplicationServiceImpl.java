package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATE_FORMAT_ERROR;

import com.sms.eagle.eye.backend.common.enums.AlertUniqueField;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.AlertService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.event.TaskAlertEvent;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.model.TaskAlarmInfo;
import com.sms.eagle.eye.backend.request.alert.AlertListRequest;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import io.vavr.control.Try;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertApplicationServiceImpl implements AlertApplicationService, ApplicationContextAware {

    public static final Integer DEFAULT_INTERVAL = 3;

    private final AlertService alertService;
    private final DataApplicationService dataApplicationService;
    private final TaskService taskService;

    private ApplicationContext applicationContext;

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

    /**
     * 解析告警信息
     * 包括 从第三方webhook接收的事件
     *  和 从AWS SQS接收的事件
     * uniqueField:
     *  - MAPPING_ID
     *      表示第三方唯一id，或任务告警规则id.
     *  - NAME
     *      表示任务名称.
     */
    @Override
    public boolean resolveWebHook(WebHookRequest request) {
        Optional<TaskAlarmInfo> taskAlarmInfoOptional = AlertUniqueField
            .resolve(request.getUniqueField().toUpperCase(Locale.ROOT)).getGetTaskAlarmInfoFunction()
            .apply(dataApplicationService, request.getUniqueValue(), request.getAlarmLevel());
        taskAlarmInfoOptional.ifPresent(taskAlarmInfo -> {
            TaskEntity task = taskService.getEntityById(taskAlarmInfo.getTaskId());
            alertService.saveAlert(task, request, taskAlarmInfo.getAlarmLevel());
            applicationContext.publishEvent(TaskAlertEvent.builder()
                .task(task)
                .alarmLevel(taskAlarmInfo.getAlarmLevel())
                .alarmMessage(request.getAlarmMessage())
                .build());
        });
        return true;
    }

    @Override
    public List<AlertResponse> list(AlertListRequest request) {
        LocalDate fromDate = convertToLocalDate(request.getFromDate());
        LocalDate toDate = convertToLocalDate(request.getToDate());
        return alertService.getResponseList(fromDate, toDate);
    }

    private static LocalDate convertToLocalDate(String date) {
        return Try.of(() -> LocalDate.parse(date)).getOrElseThrow(() -> new EagleEyeException(DATE_FORMAT_ERROR));
    }

    private Optional<LocalDate> getDateIfExceedMaxInterval(LocalDate from, LocalDate to) {
        LocalDate largestInterval = from.plusMonths(DEFAULT_INTERVAL);
        if (largestInterval.isBefore(to)) {
            return Optional.of(largestInterval);
        }
        return Optional.empty();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}