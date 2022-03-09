package com.sms.eagle.eye.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertInfo {

    private String taskName;
    private String project;
    private String alarmLevel;
    private String alarmMessage;
}