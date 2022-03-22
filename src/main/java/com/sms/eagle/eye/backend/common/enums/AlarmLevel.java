package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.INVALID_ALARM_LEVEL;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AlarmLevel {
    NORMAL(0, "Normal", Boolean.FALSE, "#deecdc"),
    WARNING(1, "Warning", Boolean.TRUE, "#faeccc"),
    CRITICAL(2, "Critical", Boolean.TRUE, "#f5dfcc");

    private final Integer value;
    private final String name;
    private final Boolean isAlarm;
    private final String color;

    private static final Map<Integer, AlarmLevel> MAP;

    static {
        MAP = Arrays.stream(values()).collect(Collectors.toMap(AlarmLevel::getValue, Function.identity()));
    }

    AlarmLevel(Integer value, String name, Boolean isAlarm, String color) {
        this.value = value;
        this.name = name;
        this.isAlarm = isAlarm;
        this.color = color;
    }

    public static AlarmLevel resolve(int value) {
        return Optional.ofNullable(MAP.get(value)).orElseThrow(() -> new EagleEyeException(INVALID_ALARM_LEVEL));
    }
}
