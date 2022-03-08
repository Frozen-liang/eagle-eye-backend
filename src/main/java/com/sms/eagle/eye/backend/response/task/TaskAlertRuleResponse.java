package com.sms.eagle.eye.backend.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private List<PluginConfigRuleWithValueResponse> alertRules;
    private List<PluginSelectOptionResponse> options;

    private Integer scheduleInterval;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer scheduleUnit;
}