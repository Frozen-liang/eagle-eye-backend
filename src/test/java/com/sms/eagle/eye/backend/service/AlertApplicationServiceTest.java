package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl;
import io.vavr.Function2;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATE_FORMAT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AlertApplicationServiceTest {

    private final AlertService alertService = mock(AlertService.class);
    private static final DataApplicationService dataApplicationService = mock(DataApplicationService.class);
    private final TaskService taskService = mock(TaskService.class);
    private final AlertApplicationService alertApplicationService =
            new AlertApplicationServiceImpl(alertService, dataApplicationService, taskService);
    private final AlertQueryRequest alertQueryRequest = AlertQueryRequest.builder().build();
    private final TaskEntity task = TaskEntity.builder().id(ID).build();
    private final AlertListRequest alertListRequest = mock(AlertListRequest.class);
    private final WebHookRequest webHookRequest = mock(WebHookRequest.class);
    private static final AlertUniqueField ALERT_UNIQUE_FIELD = mock(AlertUniqueField.class);
    private static final MockedStatic<AlertUniqueField> ALERT_UNIQUE_FIELD_MOCKED_STATIC
            = mockStatic(AlertUniqueField.class);

    private static final String KEY = "STRING";
    private static final String STRING = "STRING";
    private static final String VALUE = "String";

    private static final Long ID = 4L;
    private static final Long LONG = 1L;
    private static final Integer DEFAULT_INTERVAL = 3;
    private static final String DATA = LocalDate.now().toString();

    static {
        ALERT_UNIQUE_FIELD_MOCKED_STATIC.when(() -> AlertUniqueField.resolve(KEY)).thenReturn(ALERT_UNIQUE_FIELD);
    }

    @Test
    @DisplayName("Test the page method in the AlertApplication Service")
    public void page_test() {
        when(alertService.getPage(alertQueryRequest)).thenReturn(new Page<>());
        CustomPage<AlertResponse> page = alertApplicationService.page(alertQueryRequest);
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Test the resolveWebHook method in the AlertApplication Service")
    public void resolveWebHook() {
        Optional<Long> optional = Optional.of(ID);
        when(webHookRequest.getUniqueField()).thenReturn(STRING);
        when(webHookRequest.getUniqueValue()).thenReturn(VALUE);
        Function2<DataApplicationService, String, Optional<Long>> function2 = mock(Function2.class);
        when(ALERT_UNIQUE_FIELD.getGetTaskId()).thenReturn(function2);
        when(function2.apply(dataApplicationService, VALUE)).thenReturn(optional);
        when(taskService.getEntityById(ID)).thenReturn(task);
        doNothing().when(alertService).saveAlert(any(), any());
        assertThat(alertApplicationService.resolveWebHook(webHookRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the list method in the AlertApplication Service")
    public void list_test() {
        convertToLocalDate(DATA);
        getDateIfExceedMaxInterval(convertToLocalDate(DATA), convertToLocalDate(DATA));
        when(alertListRequest.getFromDate()).thenReturn(DATA);
        when(alertListRequest.getToDate()).thenReturn(DATA);
        when(alertService.getResponseList(any(), any())).thenReturn(new ArrayList<>());
        assertThat(alertApplicationService.list(alertListRequest)).isNotNull();
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
