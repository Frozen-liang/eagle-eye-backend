package com.sms.eagle.eye.backend.common.enums;

import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PermissionType {

    // 任务中心
    //TASK_VIEWS("Task_Views", "View permissions for alarm tasks!"),
    TASK_ADD("Task_Add", "Add permissions for alarm tasks!"),
    TASK_EDIT("Task_Edit", "Edit permissions for alarm tasks!"),
    TASK_DELETE("Task_Delete", "Delete permissions for alarm tasks!"),
    TASK_START_CLOSE("Task_Start_Close", "Permission to enable and disable alarm tasks!"),
    ALARM_HANDLE("Alarm_Handle", "Permission to handle alarm tasks!"),

    // 密码库
    //PASSWORD_VIEWS("Password_Views", "View Permission for the password vault!"),
    PASSWORD_ADD("Password_Add", "Add Permission for the password vault!"),
    PASSWORD_EDIT("Password_Edit", "Edit Permission for the password vault!"),
    PASSWORD_DELETE("Password_Delete", "Delete Permission for the password vault!"),

    // 任务组
    //TASK_GROUP_VIEWS("Task_Group_Views", "View Permission for task group!"),
    TASK_GROUP_ADD("Task_Group_Add", "Add Permission for task group!"),
    TASK_GROUP_EDIT("Task_Group_Edit", "Edit Permission for task group!"),
    TASK_GROUP_DELETE("Task_Group_Delete", "Delete Permission for task group!"),

    // 告警通道
    //ALARM_CHANNEL_VIEWS("Alarm_Channel_Views", "View Permission for alarm channel!"),
    ALARM_CHANNEL_ADD("Alarm_Channel_Add", "Add Permission for alarm channel!"),
    ALARM_CHANNEL_EDIT("Alarm_Channel_Edit", "Edit Permission for alarm channel!"),
    ALARM_CHANNEL_DELETE("Alarm_Channel_Delete", "Delete Permission for alarm channel!"),

    // 消息模板
    //MESSAGE_TEMPLATE_VIEWS("Message_Template_Views", "View Permission for message template!"),
    MESSAGE_TEMPLATE_EDIT("Message_Template_Edit", "Edit Permission for message template!"),

    // 插件配置
    //PLUGIN_VIEWS("Plugin_Views", "View Permission for plugin!"),
    PLUGIN_ADD("Plugin_Add", "Add Permission for plugin!"),
    PLUGIN_EDIT("Plugin_Edit", "Edit Permission for plugin!"),
    PLUGIN_DELETE("Plugin_Delete", "Delete Permission for plugin!"),
    PLUGIN_ENABLE_DISABLE("Plugin_Enable_Disable", "Permission to enable and disable plugins!"),

    // 用户编辑
    //USER_VIEWS("User_Views", "View Permission for user!"),
    USER_EDIT("User_Edit", "Edit Permission for user!"),

    // 权限组
    PERMISSION_VIEWS("Permission_Views", "View Permission for user and permission group!"),
    PERMISSION_GROUP_ADD("Permission_Group_Add", "Add Permission for permission group!"),
    PERMISSION_GROUP_EDIT("Permission_Group_Edit", "Edit Permission for permission group!"),
    PERMISSION_GROUP_DELETE("Permission_Group_Delete", "Delete Permission for permission group!");

    private final String permission;
    private final String description;

    public static final List<PermissionResponse> PERMISSION_RESPONSES =
        Arrays.stream(values()).map(PermissionType::buildPermissionResponse).collect(Collectors.toUnmodifiableList());

    PermissionType(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    private PermissionResponse buildPermissionResponse() {
        return PermissionResponse.builder().name(getPermission()).description(getDescription()).build();
    }

}
