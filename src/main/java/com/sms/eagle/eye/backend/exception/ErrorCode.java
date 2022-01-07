package com.sms.eagle.eye.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    OAUTH_CODE_ERROR("10000", "OAuth failed, invalid grant"),
    DATABASE_OPERATION_FAILURE("9999", "Database exceptions"),
    PLUGIN_SERVER_URL_ERROR("1000", "Wrong address for plugin service"),
    PLUGIN_CONFIG_FIELD_MISSING_ERROR("", "%s is required, please fill in the configuration"),
    ALERT_RULE_FIELD_MISSING_ERROR("", "%s is required, please fill in the configuration"),
    TASK_ID_IS_NOT_CORRECT("", "Can't find the corresponding monitoring task"),
    PLUGIN_ID_IS_NOT_CORRECT("", "Can't find the corresponding plugin"),
    INVALID_TASK_SCHEDULE_UNIT_ID("", "");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}