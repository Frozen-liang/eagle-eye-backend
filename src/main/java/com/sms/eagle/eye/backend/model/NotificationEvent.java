package com.sms.eagle.eye.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private Integer channelType;
    private String channelConfig;
    private String channelInput;

    private String contentTemplate;
    private String variableKey;
    private Object variable;

}