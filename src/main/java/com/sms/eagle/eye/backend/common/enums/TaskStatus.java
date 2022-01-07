package com.sms.eagle.eye.backend.common.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    AWAITING(1, "Awaiting"),
    RUNNING(2, "Running"),
    ERROR(3, "Error");

    private final Integer value;
    private final String name;

    TaskStatus(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
}
