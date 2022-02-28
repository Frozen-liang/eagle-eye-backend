package com.sms.eagle.eye.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertRule {

    private Long ruleId;

    private Integer alarmLevel;
    private String decryptedAlertRule;

    private Integer scheduleInterval;
    private Integer scheduleUnit;
}