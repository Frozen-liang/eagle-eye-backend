package com.sms.eagle.eye.backend.response.plugin;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertRuleResponse {

    /**
     * 系统告警级别.
     */
    @NotBlank
    private String alarmLevel;
    /**
     * 插件metadata 提供的告警规则的 key列表.
     */
    @NotEmpty
    private List<PluginConfigRuleResponse> alerts;
}