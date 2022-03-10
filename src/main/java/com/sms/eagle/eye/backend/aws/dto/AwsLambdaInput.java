package com.sms.eagle.eye.backend.aws.dto;

import java.util.List;
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
    private String pluginUrl;
    private String queueUrl;
    private List<AwsAlertRuleDto> alertRule;
}