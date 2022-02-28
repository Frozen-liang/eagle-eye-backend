package com.sms.eagle.eye.backend.response.task;

import com.sms.eagle.eye.backend.response.plugin.PluginConfigRuleResponse;
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
public class PluginConfigRuleWithValueResponse extends PluginConfigRuleResponse {

    public static final String DEFAULT_VALUE = "";

    @Builder.Default
    private String value = DEFAULT_VALUE;
}