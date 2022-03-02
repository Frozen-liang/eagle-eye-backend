package com.sms.eagle.eye.backend.response.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertNotification {

    private String taskName;
    private String project;
    private String description;
    private String pluginName;
    private String alarmLevel;
    private String alertTime;
}