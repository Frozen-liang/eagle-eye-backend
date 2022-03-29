package com.sms.eagle.eye.backend.utils;

import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.model.TaskAlertRule;

public class TaskScheduleUtil {

    private TaskScheduleUtil() {}

    public static Integer getMinuteInterval(TaskAlertRule taskAlertRule) {
        return TaskScheduleUnit.resolve(taskAlertRule.getScheduleUnit())
            .getConvertToMinute().apply(taskAlertRule.getScheduleInterval());
    }
}