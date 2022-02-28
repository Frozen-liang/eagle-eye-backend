package com.sms.eagle.eye.backend.request.plugin;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginRequest {

    /**
     * 插件服务地址.
     */
    @NotNull
    private String url;
    /**
     * 插件告警级别与系统告警级别的映射
     * （scheduleBySelf为true时需要设置）.
     */
    private List<AlarmLevelMappingRequest> alarmLevelMapping;
    /**
     * 将 告警规则 绑定至 系统告警级别上.
     */
    private List<AlertRuleRequest> alertRule;
}