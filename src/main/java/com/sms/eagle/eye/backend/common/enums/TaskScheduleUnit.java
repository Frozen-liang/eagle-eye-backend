package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.INVALID_TASK_SCHEDULE_UNIT_ID;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum TaskScheduleUnit {
    MINUTE(0, "Minute", TaskScheduleUnit::minuteToMinute),
    HOUR(1, "Hour", TaskScheduleUnit::hourToMinute),
    DAY(2, "Day", TaskScheduleUnit::dayToMinute),
    MONTH(3, "Month", TaskScheduleUnit::monthToMinute);

    private static final Map<Integer, TaskScheduleUnit> MAPPING;

    static {
        MAPPING = Arrays.stream(values())
            .collect(Collectors.toMap(TaskScheduleUnit::getId, Function.identity()));
    }

    private final Integer id;
    private final String name;
    private final Function<Integer, Integer> convertToMinute;

    TaskScheduleUnit(int id, String name,
        Function<Integer, Integer> convertToMinute) {
        this.id = id;
        this.name = name;
        this.convertToMinute = convertToMinute;
    }

    private static Integer minuteToMinute(Integer minute) {
        return minute;
    }

    private static Integer hourToMinute(Integer hour) {
        return minuteToMinute(60);
    }

    private static Integer dayToMinute(Integer day) {
        return hourToMinute(24);
    }

    private static Integer monthToMinute(Integer month) {
        return dayToMinute(30);
    }

    public static TaskScheduleUnit resolve(Integer id) {
        Optional<TaskScheduleUnit> optional = Optional.ofNullable(MAPPING.get(id));
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EagleEyeException(INVALID_TASK_SCHEDULE_UNIT_ID);
    }
}
