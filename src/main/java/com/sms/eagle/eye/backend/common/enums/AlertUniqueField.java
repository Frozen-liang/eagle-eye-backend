package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.INVALID_ALERT_FIELD_KEY;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import io.vavr.Function2;
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

    private static Optional<Long> getTaskIdByMappingId(DataApplicationService service, String value) {
        return service.getTaskByMappingId(value);
    }

    private static Optional<Long> getTaskIdByTaskName(DataApplicationService service, String value) {
        return service.getTaskIdByTaskName(value);
    }

    private final String key;
    private final Function2<DataApplicationService, String, Optional<Long>> getTaskId;

    AlertUniqueField(String key,
        Function2<DataApplicationService, String, Optional<Long>> getTaskId) {
        this.key = key;
        this.getTaskId = getTaskId;
    }

    public static AlertUniqueField resolve(String key) {
        return Optional.ofNullable(MAP.get(key)).orElseThrow(() -> new EagleEyeException(INVALID_ALERT_FIELD_KEY));
    }
}
