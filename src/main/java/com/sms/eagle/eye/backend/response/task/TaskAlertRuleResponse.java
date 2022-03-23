package com.sms.eagle.eye.backend.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertRuleResponse {

    /**
     *  告警规则.
     */
    private List<PluginAlertRuleWithValueResponse> alertRules;
    private List<PluginSelectOptionResponse> options;
    @Builder.Default
    private Integer scheduleInterval = 5;
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer scheduleUnit = TaskScheduleUnit.MINUTE.getId();
}