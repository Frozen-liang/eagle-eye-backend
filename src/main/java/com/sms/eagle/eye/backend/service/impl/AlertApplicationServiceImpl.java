package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATE_FORMAT_ERROR;

import com.sms.eagle.eye.backend.common.enums.AlertUniqueField;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.AlertService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.CustomPage;
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
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertApplicationServiceImpl implements AlertApplicationService {

    public static final Integer DEFAULT_INTERVAL = 3;

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

    @Override
    public List<AlertResponse> list(AlertListRequest request) {
        LocalDate fromDate = convertToLocalDate(request.getFromDate());
        LocalDate toDate = convertToLocalDate(request.getToDate());
        Optional<LocalDate> optional = getDateIfExceedMaxInterval(fromDate, toDate);
        return alertService.getResponseList(fromDate, optional.orElse(toDate));
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
}