package com.sms.eagle.eye.backend.service;

import static com.sms.eagle.eye.backend.exception.ErrorCode.DATE_FORMAT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl;
import io.vavr.Function3;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;

public class AlertApplicationServiceTest {

    private static final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private static final AlertService alertService = mock(AlertService.class);
    private static final DataApplicationService dataApplicationService = mock(DataApplicationService.class);
    private static final TaskService taskService = mock(TaskService.class);

    private static final AlertApplicationServiceImpl alertApplicationService =
        spy(new AlertApplicationServiceImpl(alertService, dataApplicationService, taskService));

    private static final MockedStatic<AlertUniqueField> ALERT_UNIQUE_FIELD_MOCKED_STATIC
        = mockStatic(AlertUniqueField.class);

    private static final String EXCEPTION_CODE = "code";

    @BeforeAll
    public static void init() {
        alertApplicationService.setApplicationContext(applicationContext);
    }

    @AfterAll
    public static void close() {
        ALERT_UNIQUE_FIELD_MOCKED_STATIC.close();
    }

    /**
     * {@link AlertApplicationServiceImpl#page(AlertQueryRequest)}.
     *
     * <p>?????? Query?????? ????????????????????????
     */
    @Test
    @DisplayName("Test the page method in the AlertApplication Service")
    public void page_test() {
        // ??????????????????
        AlertQueryRequest alertQueryRequest = AlertQueryRequest.builder().build();
        // mock IPage<AlertResponse> ??????
        IPage<AlertResponse> iPage = mock(IPage.class);
        List<AlertResponse> list = mock(List.class);
        doReturn(list).when(iPage).getRecords();
        // mock alertService.getPage() ??????
        when(alertService.getPage(alertQueryRequest)).thenReturn(iPage);
        // ??????
        CustomPage<AlertResponse> result = alertApplicationService.page(alertQueryRequest);
        // Assert
        assertThat(result.getRecords()).isEqualTo(list);
    }

    /**
     * {@link AlertApplicationServiceImpl#resolveWebHook(WebHookRequest)}.
     *
     * <p>????????????????????????????????????webhook??????????????????
     * ?????????AWS SQS???????????????.
     *
     * <p>??????1????????? uniqueField???uniqueValue ??? alarmLevel
     * ?????????????????? {@link TaskAlarmInfo}??? ???????????????????????????????????????????????????????????????
     */
    @Test
    @DisplayName("Test the resolveWebHook method in the AlertApplication Service")
    public void resolveWebHook_1() {
        // ??????????????????
        String uniqueField = "mappingId";
        String alarmLevel = "alarmLevel";
        String uniqueValue = "uniqueValue";
        WebHookRequest webHookRequest = WebHookRequest.builder()
            .uniqueField(uniqueField)
            .uniqueValue(uniqueValue)
            .alarmLevel(alarmLevel)
            .build();
        // mock???????????? AlertUniqueField.resolve
        AlertUniqueField alertUniqueField = mock(AlertUniqueField.class);
        ALERT_UNIQUE_FIELD_MOCKED_STATIC.when(() -> AlertUniqueField.resolve(uniqueField.toUpperCase(Locale.ROOT)))
            .thenReturn(alertUniqueField);
        // mock alertUniqueField.getGetTaskAlarmInfoFunction() ??????
        Function3<DataApplicationService, String, String, Optional<TaskAlarmInfo>> function3 = mock(Function3.class);
        doReturn(function3).when(alertUniqueField).getGetTaskAlarmInfoFunction();
        // mock apply() ??????
        Long taskId = 1L;
        Integer alarm = 1;
        TaskAlarmInfo taskAlarmInfo = TaskAlarmInfo.builder()
            .taskId(taskId).alarmLevel(alarm).build();
        Optional<TaskAlarmInfo> optional = Optional.ofNullable(taskAlarmInfo);
        doReturn(optional).when(function3).apply(dataApplicationService,
            webHookRequest.getUniqueValue(), webHookRequest.getAlarmLevel());
        // mock taskService.getEntityById() ??????
        TaskEntity taskEntity = mock(TaskEntity.class);
        doReturn(taskEntity).when(taskService).getEntityById(taskId);
        // mock alertService.saveAlert() ??????
        doNothing().when(alertService).saveAlert(taskEntity, webHookRequest, taskAlarmInfo.getAlarmLevel());
        // mock applicationContext.publishEvent() ??????
        doNothing().when(applicationContext).publishEvent(any(TaskAlertEvent.class));
        // ??????
        alertApplicationService.resolveWebHook(webHookRequest);
        // Assert
        assertThat(alertApplicationService.resolveWebHook(webHookRequest)).isTrue();
    }

    /**
     * {@link AlertApplicationServiceImpl#resolveWebHook(WebHookRequest)}.
     *
     * <p>??????2????????? uniqueField???uniqueValue ??? alarmLevel
     * ?????????????????? {@link TaskAlarmInfo}?????????????????????
     */
    @Test
    public void resolveWebHook_2() {
        // ??????????????????
        String uniqueField = "mappingId";
        String alarmLevel = "alarmLevel";
        String uniqueValue = "uniqueValue";
        WebHookRequest webHookRequest = WebHookRequest.builder()
            .uniqueField(uniqueField)
            .uniqueValue(uniqueValue)
            .alarmLevel(alarmLevel)
            .build();
        // mock???????????? AlertUniqueField.resolve
        AlertUniqueField alertUniqueField = mock(AlertUniqueField.class);
        ALERT_UNIQUE_FIELD_MOCKED_STATIC.when(() -> AlertUniqueField.resolve(uniqueField.toUpperCase(Locale.ROOT)))
            .thenReturn(alertUniqueField);
        // mock alertUniqueField.getGetTaskAlarmInfoFunction() ??????
        Function3<DataApplicationService, String, String, Optional<TaskAlarmInfo>> function3 = mock(Function3.class);
        doReturn(function3).when(alertUniqueField).getGetTaskAlarmInfoFunction();
        // mock apply() ??????
        doReturn(Optional.empty()).when(function3).apply(dataApplicationService,
            webHookRequest.getUniqueValue(), webHookRequest.getAlarmLevel());
        // ??????
        alertApplicationService.resolveWebHook(webHookRequest);
        // Assert
        assertThat(alertApplicationService.resolveWebHook(webHookRequest)).isTrue();
    }

    /**
     * {@link AlertApplicationServiceImpl#list(AlertListRequest)}
     *
     * <p> ?????? {@link AlertListRequest} ????????????????????????
     *
     * <p> ??????1???????????????????????????????????????????????????
     */
    @Test
    public void list_test_1() {
        // ??????????????????
        String fromDate = "fromDate";
        String toDate = "toDate";
        AlertListRequest request = AlertListRequest.builder().fromDate(fromDate).toDate(toDate).build();
        // ????????????
        assertThatThrownBy(() -> alertApplicationService.list(request)).isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(DATE_FORMAT_ERROR.getCode());
    }

    /**
     * {@link AlertApplicationServiceImpl#list(AlertListRequest)}
     *
     * <P> ??????2???{@link AlertListRequest} ???????????? fromDate ??? toDate
     * ?????????????????????????????? [fromDate -> toDate] ????????????????????????
     */
    @Test
    public void list_test_3() {
        // ??????????????????
        String fromDate = "2022-01-01";
        String toDate = "2022-03-01";
        AlertListRequest request = AlertListRequest.builder().fromDate(fromDate).toDate(toDate).build();
        // mock alertService.getResponseList() ??????
        List<AlertResponse> list = mock(List.class);
        doReturn(list).when(alertService).getResponseList(
            LocalDate.parse(fromDate), LocalDate.parse(toDate));
        // ??????
        List<AlertResponse> result = alertApplicationService.list(request);
        // ??????
        assertThat(result).isEqualTo(list);
    }
}
