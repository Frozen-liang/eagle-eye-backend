package com.sms.eagle.eye.backend.common.enums;

import lombok.Getter;

@Getter
public enum AlarmLevel {
    NORMAL(0, "Normal", Boolean.FALSE, "#deecdc"),
    TIPS(1, "Tips", Boolean.TRUE, "#faeccc"),
    IMPORTANT(2, "Important", Boolean.TRUE, "#f5dfcc"),
    EMERGENCY(3, "Emergency", Boolean.TRUE, "#fae3de");

    private final Integer value;
    private final String name;
    private final Boolean isAlarm;
    private final String color;

    AlarmLevel(Integer value, String name, Boolean isAlarm, String color) {
        this.value = value;
        this.name = name;
        this.isAlarm = isAlarm;
        this.color = color;
    }
}
