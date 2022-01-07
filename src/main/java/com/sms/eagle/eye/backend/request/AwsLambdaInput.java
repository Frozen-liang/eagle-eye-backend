package com.sms.eagle.eye.backend.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwsLambdaInput {

    private String id;
    private String name;
    private String description;
    private String decryptKey;
    private String payload;
    private Integer interval;
    private String pluginUrl;
    private String updateUrl;
    private String webhookUrl;
}