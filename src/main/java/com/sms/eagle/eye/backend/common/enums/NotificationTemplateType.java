package com.sms.eagle.eye.backend.common.enums;

import com.sms.eagle.eye.backend.model.TemplateField;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {
    ALERT(1, "Alert", Collections.emptyList());

    private final int value;
    private final String name;
    private final List<TemplateField> fieldList;

    NotificationTemplateType(int value, String name,
        List<TemplateField> fieldList) {
        this.value = value;
        this.name = name;
        this.fieldList = fieldList;
    }
}
