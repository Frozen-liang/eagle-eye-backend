package com.sms.eagle.eye.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UN_SUPPORT_OPERATION("", "Unsupported operations"),
    OAUTH_CODE_ERROR("10000", "OAuth failed, invalid grant"),
    DATABASE_OPERATION_FAILURE("9999", "Database exceptions"),
    PLUGIN_SERVER_URL_ERROR("1000", "Wrong address for plugin service"),
    PLUGIN_CONFIG_FIELD_MISSING_ERROR("", "%s is required, please fill in the configuration"),
    ALERT_RULE_FIELD_MISSING_ERROR("", "%s is required, please fill in the configuration"),
    CHANNEL_TYPE_IS_NOT_CORRECT("", "Can't find the corresponding channel"),
    CHANNEL_ID_IS_NOT_CORRECT("", "The channel does not exist or has been deleted"),
    TASK_ID_IS_NOT_CORRECT("", "Can't find the corresponding monitoring task"),
    TASK_ALERT_NOTIFICATION_ID_IS_NOT_CORRECT("", "Can't find the corresponding alert notification"),
    GROUP_ID_IS_NOT_CORRECT("", "Can't find the corresponding group"),
    PLUGIN_ID_IS_NOT_CORRECT("", "Can't find the corresponding plugin"),
    PLUGIN_HAS_ALREADY_EXIST("", "Plugin name already exists"),
    MUST_USE_PASSWORD_VAULT("", "You need to use the passwords in the password vault"),
    INVALID_TASK_SCHEDULE_UNIT_ID("", ""),
    INVALID_ALERT_FIELD_KEY("", ""),
    INVALID_ALARM_LEVEL("", ""),
    TASK_HANDLER_IS_NOT_EXIST("", ""),
    PLUGIN_CLIENT_SHUTDOWN_ERROR("", ""),
    TASK_IS_RUNNING_AND_DELETE_ERROR("", "Task is running, please stop first"),
    TASK_NAME_HAS_ALREADY_EXIST("", "Task name already exists, please set to other"),
    TASK_STATUS_VALUE_ERROR("", ""),
    TASK_IS_ALREADY_RUNNING("", "Task is already running"),
    TASK_IS_ALREADY_NON_RUNNING("", "The task is already in a non-running state"),
    PASSWORD_KEY_HAS_ALREADY_EXIST("", ""),
    PASSWORD_KEY_IS_NOT_EXIST("", ""),
    GROUP_NAME_HAS_ALREADY_EXIST("", "Group name already exists, please set to other"),
    REMOVE_CHILD_BEFORE_DELETE_GROUP("", "Please delete sub-Groups first"),
    DATE_FORMAT_ERROR("", "Please enter the correct date format: yy-MM-dd"),
    GET_METADATA_ERROR("", "Failed to get metadata"),
    CHANNEL_CHECK_ERROR("", "%s can not be empty"),
    WECOM_ERROR("", "Failed to send WeCom message: %s"),
    REMOVE_TASK_BEFORE_DELETE_GROUP("", "Please delete the associated task first");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
