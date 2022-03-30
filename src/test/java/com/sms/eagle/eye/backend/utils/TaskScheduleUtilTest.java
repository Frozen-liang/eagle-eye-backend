package com.sms.eagle.eye.backend.utils;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class TaskScheduleUtilTest {

    private static final MockedStatic<TaskScheduleUnit> TASK_SCHEDULE_UNIT_MOCKED_STATIC
        = mockStatic(TaskScheduleUnit.class);

    @AfterAll
    public static void close() {
        TASK_SCHEDULE_UNIT_MOCKED_STATIC.close();
    }

    /**
     * {@link TaskScheduleUtil#getMinuteInterval(TaskAlertRule)}
     */
    @Test
    void getMinuteInterval_test() {
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getScheduleUnit()
        Integer scheduleUnit = 1;
        when(taskAlertRule.getScheduleUnit()).thenReturn(scheduleUnit);
        // mock taskAlertRule.getScheduleInterval()
        Integer scheduleInterval = 2;
        when(taskAlertRule.getScheduleInterval()).thenReturn(scheduleInterval);
        // mock static TaskScheduleUnit.resolve
        TaskScheduleUnit taskScheduleUnit = mock(TaskScheduleUnit.class);
        TASK_SCHEDULE_UNIT_MOCKED_STATIC.when(() -> TaskScheduleUnit.resolve(scheduleUnit))
            .thenReturn(taskScheduleUnit);
        // mock taskScheduleUnit.getConvertToMinute()
        Function<Integer, Integer> convertToMinute = mock(Function.class);
        when(taskScheduleUnit.getConvertToMinute()).thenReturn(convertToMinute);
        // mock convertToMinute.apply()
        Integer minuteInterval = 2;
        when(convertToMinute.apply(scheduleInterval)).thenReturn(minuteInterval);
        // invoke
        Integer result = TaskScheduleUtil.getMinuteInterval(taskAlertRule);
        // assert
        assertThat(result).isEqualTo(minuteInterval);
    }
}