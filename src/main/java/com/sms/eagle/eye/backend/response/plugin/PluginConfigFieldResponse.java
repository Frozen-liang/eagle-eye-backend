package com.sms.eagle.eye.backend.response.plugin;

import com.sms.eagle.eye.plugin.v1.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PluginConfigFieldResponse {

    private String key;
    private String labelName;
    /**
     * 字段类型 INPUT = 1; PASSWORD = 2; SQL_EDITOR = 3; LOG_EDITOR = 4; RULE_EDITOR = 5;.
     */
    @Builder.Default
    private Integer type = FieldType.INPUT_VALUE;
    private String option;
    /**
     * 默认值.
     */
    private String defaultValue;
    /**
     * 是否必须.
     */
    @Builder.Default
    private Boolean required = Boolean.FALSE;
    /**
     * 显示顺序.
     */
    private Integer order;
}