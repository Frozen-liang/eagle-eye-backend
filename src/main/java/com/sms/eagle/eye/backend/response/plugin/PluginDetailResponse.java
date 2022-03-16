package com.sms.eagle.eye.backend.response.plugin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginDetailResponse {

    private String name;
    private String description;
    private Integer version;
    private Boolean scheduleBySelf;
    /**
     * 配置字段.
     */
    private List<PluginConfigFieldResponse> fields;
    /**
     * 选择数据.
     */
    private List<PluginSelectOptionResponse> options;
    private List<AlarmLevelMappingResponse> alarmLevelMapping;
    private List<AlertRuleResponse> alertRule;
    /**
     * 插件所有的告警规则，供编辑时候进行选择.
     */
    private List<PluginAlertRuleFieldResponse> allAlerts;
}