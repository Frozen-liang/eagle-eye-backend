package com.sms.eagle.eye.backend.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sms.eagle.eye.backend.utils.KeepAsJsonDeserializer;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertRuleRequest {

    /**
     * 任务id.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskId;
    /**
     * 告警级别.
     */
    @NotNull
    private Integer alarmLevel;
    /**
     * 任务告警规则.
     */
    @NotNull
    @JsonDeserialize(using = KeepAsJsonDeserializer.class)
    private String alertRules;
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