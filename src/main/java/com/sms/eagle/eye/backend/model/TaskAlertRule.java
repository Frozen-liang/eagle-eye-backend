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

    /**
     * 如果插件做了级别映射，则返回映射的级别
     * 没有则返回当前系统的告警级别.
     */
    private String alarmLevel;
    private String decryptedAlertRule;

    private Integer scheduleInterval;
    private Integer scheduleUnit;
}