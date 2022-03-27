package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.NOTIFICATION_TEMPLATE_TYPE_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.TemplateField;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum NotificationTemplateType {
    ALERT(1, "Alert", "alert", NotificationTemplateType.alertList());

    private final int value;
    private final String name;
    private final String variableKey;
    private final List<TemplateField> fieldList;
    private static final Map<Integer, NotificationTemplateType> MAP;

    static {
        MAP = Arrays.stream(values())
            .collect(Collectors.toMap(NotificationTemplateType::getValue, Function.identity()));
    }

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

    public static NotificationTemplateType resolve(Integer value) {
        return Optional.ofNullable(MAP.get(value))
            .orElseThrow(() -> new EagleEyeException(NOTIFICATION_TEMPLATE_TYPE_ERROR));
    }
}
