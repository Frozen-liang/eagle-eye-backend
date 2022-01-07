package com.sms.eagle.eye.backend.common.enums;

import lombok.Getter;

@Getter
public enum ThirdPartyType {
    AWS_EVENT_BRIDGE_RULE(1, "AWS EventBridge Rule"),
    PLUGIN_SYSTEM_UNION_ID(2, "");

    private final Integer value;
    private final String description;

    ThirdPartyType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
