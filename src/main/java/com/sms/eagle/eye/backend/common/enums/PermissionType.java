package com.sms.eagle.eye.backend.common.enums;

public enum PermissionType {

    // 任务中心
    TASK_VIEWS(1L, "Task_Views", "View permissions for alarm tasks!"),
    TASK_ADD(3L, "Task_Add", "Add permissions for alarm tasks!"),
    TASK_EDIT(2L, "Task_Edit", "Edit permissions for alarm tasks!"),
    TASK_DELETE(4L, "Task_Delete", "Delete permissions for alarm tasks!"),
    TASK_START_CLOSE(5L, "Task_Start_Close", "Permission to enable and disable alarm tasks!"),
    ALARM_HANDLE(6L, "Alarm_Handle", "Permission to handle alarm tasks!"),

    // 密码库
    PASSWORD_VIEWS(6L, "Password_Views", "View Permission for the password vault!"),
    PASSWORD_ADD(6L, "Password_Add", "Add Permission for the password vault!"),
    PASSWORD_EDIT(6L, "Password_Edit", "Edit Permission for the password vault!"),
    PASSWORD_DELETE(6L, "Password_Delete", "Delete Permission for the password vault!"),

    // 任务组
    TASK_GROUP_VIEWS(6L, "Task_Group_Views", "View Permission for task group!"),
    TASK_GROUP_ADD(6L, "Task_Group_Add", "Add Permission for task group!"),
    TASK_GROUP_EDIT(6L, "Task_Group_Edit", "Edit Permission for task group!"),
    TASK_GROUP_DELETE(6L, "Task_Group_Delete", "Delete Permission for task group!"),

    // 告警通道
    ALARM_CHANNEL_VIEWS(6L, "Alarm_Channel_Views", "View Permission for alarm channel!"),
    ALARM_CHANNEL_ADD(6L, "Alarm_Channel_Add", "Add Permission for alarm channel!"),
    ALARM_CHANNEL_EDIT(6L, "Alarm_Channel_Edit", "Edit Permission for alarm channel!"),
    ALARM_CHANNEL_DELETE(6L, "Alarm_Channel_Delete", "Delete Permission for alarm channel!"),

    // 消息模板
    MESSAGE_TEMPLATE_VIEWS(6L, "Message_Template_Views", "View Permission for message template!"),
    MESSAGE_TEMPLATE_EDIT(6L, "Message_Template_Edit", "Edit Permission for message template!"),

    // 插件配置
    PLUGIN_VIEWS(6L, "Plugin_Views", "View Permission for plugin!"),
    PLUGIN_ADD(6L, "Plugin_Add", "Add Permission for plugin!"),
    PLUGIN_EDIT(6L, "Plugin_Edit", "Edit Permission for plugin!"),
    PLUGIN_DELETE(6L, "Plugin_Delete", "Delete Permission for plugin!"),
    PLUGIN_ENABLE_DISABLE(6L, "Plugin_Enable_Disable", "Permission to enable and disable plugins!"),

    // 用户编辑
    USER_VIEWS(6L, "User_Views", "View Permission for user!"),
    USER_EDIT(6L, "User_Edit", "Edit Permission for user!"),

    // 权限组
    PERMISSION_GROUP_VIEWS(6L, "Permission_Group_Views", "View Permission for permission group!"),
    PERMISSION_GROUP_ADD(6L, "Permission_Group_Add", "Add Permission for permission group!"),
    PERMISSION_GROUP_EDIT(6L, "Permission_Group_Edit", "Edit Permission for permission group!"),
    PERMISSION_GROUP_DELETE(6L, "Permission_Group_Delete", "Delete Permission for permission group!");

    private final Long id;
    private final String permission;
    private final String description;

    PermissionType(Long id, String permission, String description) {
        this.id = id;
        this.permission = permission;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }
}
