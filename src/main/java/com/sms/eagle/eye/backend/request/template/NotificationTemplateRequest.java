package com.sms.eagle.eye.backend.request.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateRequest {

    private Integer channelType;
    private Integer templateType;
    private String template;
}