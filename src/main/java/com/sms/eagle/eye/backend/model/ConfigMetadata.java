package com.sms.eagle.eye.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMetadata {

    private String key;
    private String labelName;
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
     * 是否加密.
     */
    @Builder.Default
    private Boolean encrypted = Boolean.FALSE;

}