package com.sms.eagle.eye.backend.response.task;

import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PluginAlertRuleWithValueResponse extends PluginAlertRuleFieldResponse {

    public static final Object DEFAULT_VALUE = "";

    @Builder.Default
    private Object value = DEFAULT_VALUE;
}