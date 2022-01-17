package com.sms.eagle.eye.backend.utils;

import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;

public class TaskScheduleUtil {

    private TaskScheduleUtil() {}

    public static Integer getMinuteInterval(TaskEntity task) {
        return TaskScheduleUnit.resolve(task.getScheduleUnit())
            .getConvertToMinute().apply(task.getScheduleInterval());
    }
}