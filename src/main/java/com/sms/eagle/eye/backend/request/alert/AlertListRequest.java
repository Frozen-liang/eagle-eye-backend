package com.sms.eagle.eye.backend.request.alert;

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
    private String fromDate;
    /**
     * 结束日期.
     */
    private String toDate;
}