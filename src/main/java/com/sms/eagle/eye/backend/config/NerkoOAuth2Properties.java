package com.sms.eagle.eye.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "nerko.oauth2")
public class NerkoOAuth2Properties {

    private String tokenEndpoint;
    private String clientId;
    private String clientSecret;

}