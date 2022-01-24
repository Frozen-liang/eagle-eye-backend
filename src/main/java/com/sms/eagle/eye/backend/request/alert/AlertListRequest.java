package com.sms.eagle.eye.backend.request.alert;

import static com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl.DEFAULT_INTERVAL;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertListRequest {

    /**
     * 开始日期.
     */
    @Builder.Default
    private String fromDate = LocalDate.now().minusMonths(DEFAULT_INTERVAL).toString();
    /**
     * 结束日期.
     */
    @Builder.Default
    private String toDate = LocalDate.now().toString();
}