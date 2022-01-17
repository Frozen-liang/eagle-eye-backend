package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_STATUS_VALUE_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum TaskStatus {
    AWAITING(1, "Awaiting", Boolean.FALSE),
    RUNNING(2, "Running", Boolean.TRUE),
    ERROR(3, "Error", Boolean.FALSE);

    private static final Map<Integer, TaskStatus> MAP;

    static {
        MAP = Arrays.stream(values()).collect(Collectors.toMap(TaskStatus::getValue, Function.identity()));
    }

    private final Integer value;
    private final String name;
    private final boolean runningStatus;

    TaskStatus(Integer value, String name, boolean runningStatus) {
        this.value = value;
        this.name = name;
        this.runningStatus = runningStatus;
    }

    public static TaskStatus resolve(Integer value) {
        return Optional.ofNullable(MAP.get(value)).orElseThrow(() -> new EagleEyeException(TASK_STATUS_VALUE_ERROR));
    }
}
