package com.sms.eagle.eye.backend.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskScheduleRequest {

    /**
     * 任务id.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 任务间隔数值.
     */
    @NotNull
    private Integer scheduleInterval;
    /**
     * 任务间隔单位.
     */
    @NotNull
    private Integer scheduleUnit;
}