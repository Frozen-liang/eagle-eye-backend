package com.sms.eagle.eye.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

    private String[] origins = {"*"};
    private String[] methods = {"*"};
    private String mapping = "/**";
    private Long maxAge = 3600L;
    private Boolean allowCredentials = true;
}