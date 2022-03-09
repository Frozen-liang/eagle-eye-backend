package com.sms.eagle.eye.backend.common.enums;

import com.sms.eagle.eye.backend.model.TemplateField;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {
    ALERT(1, "Alert", "alert", NotificationTemplateType.alertList());

    private final int value;
    private final String name;
    private final String variableKey;
    private final List<TemplateField> fieldList;

    private static List<TemplateField> alertList() {
        return Arrays.asList(TemplateField.builder().name("taskName").description("Task Name").build(),
            TemplateField.builder().name("project").description("Project").build(),
            TemplateField.builder().name("alarmLevel").description("Alarm Level").build(),
            TemplateField.builder().name("alarmMessage").description("Alarm Message").build());
    }

    NotificationTemplateType(int value, String name,
        String variableKey, List<TemplateField> fieldList) {
        this.value = value;
        this.name = name;
        this.variableKey = variableKey;
        this.fieldList = fieldList;
    }
}
