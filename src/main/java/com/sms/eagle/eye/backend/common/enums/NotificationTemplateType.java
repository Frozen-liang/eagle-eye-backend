package com.sms.eagle.eye.backend.common.enums;

import com.sms.eagle.eye.backend.model.TemplateField;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {
    ALERT(1, "Alert", NotificationTemplateType.alertList());

    private final int value;
    private final String name;
    private final List<TemplateField> fieldList;

    private static List<TemplateField> alertList() {
        return Arrays.asList(TemplateField.builder().name("username").description("Username").build(),
            TemplateField.builder().name("password").description("Password").build());
    }

    NotificationTemplateType(int value, String name,
        List<TemplateField> fieldList) {
        this.value = value;
        this.name = name;
        this.fieldList = fieldList;
    }
}
