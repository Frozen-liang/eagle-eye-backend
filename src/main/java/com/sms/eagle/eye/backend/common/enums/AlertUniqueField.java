package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.INVALID_ALERT_FIELD_KEY;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.TaskAlarmInfo;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import io.vavr.Function3;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AlertUniqueField {
    MAPPING_ID("MAPPINGID", AlertUniqueField::getTaskIdByMappingId),
    TASK_NAME("NAME", AlertUniqueField::getTaskIdByTaskName);

    private static final Map<String, AlertUniqueField> MAP;

    static {
        MAP = Arrays.stream(values()).collect(Collectors.toMap(AlertUniqueField::getKey, Function.identity()));
    }

    private static Optional<TaskAlarmInfo> getTaskIdByMappingId(
        DataApplicationService service, String value, String mappingLevel) {
        return service.getTaskByMappingId(value, mappingLevel);
    }

    private static Optional<TaskAlarmInfo> getTaskIdByTaskName(
        DataApplicationService service, String value, String mappingLevel) {
        return service.getTaskIdByTaskName(value, mappingLevel);
    }

    private final String key;
    private final Function3<DataApplicationService, String, String, Optional<TaskAlarmInfo>> getTaskAlarmInfoFunction;

    AlertUniqueField(String key,
        Function3<DataApplicationService, String, String, Optional<TaskAlarmInfo>> getTaskAlarmInfoFunction) {
        this.key = key;
        this.getTaskAlarmInfoFunction = getTaskAlarmInfoFunction;
    }

    public static AlertUniqueField resolve(String key) {
        return Optional.ofNullable(MAP.get(key)).orElseThrow(() -> new EagleEyeException(INVALID_ALERT_FIELD_KEY));
    }
}
