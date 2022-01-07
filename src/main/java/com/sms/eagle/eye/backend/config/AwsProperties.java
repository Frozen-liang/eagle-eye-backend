package com.sms.eagle.eye.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "eagle.aws")
public class AwsProperties {

    private String region;
    private String lambdaArn;
    private String roleArn;
    private String updateUrl;
    private String webhookUrl;
}