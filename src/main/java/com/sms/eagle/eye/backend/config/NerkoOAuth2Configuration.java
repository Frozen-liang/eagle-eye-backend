package com.sms.eagle.eye.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sh.ory.hydra.ApiClient;
import sh.ory.hydra.api.PublicApi;
import sh.ory.hydra.auth.HttpBasicAuth;

@Configuration
@EnableConfigurationProperties(NerkoOAuth2Properties.class)
public class NerkoOAuth2Configuration {

    @Bean
    public PublicApi publicApi(NerkoOAuth2Properties properties) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(properties.getBasePath());
        HttpBasicAuth basic = (HttpBasicAuth) apiClient.getAuthentication("basic");
        basic.setUsername(properties.getClientId());
        basic.setPassword(properties.getClientSecret());
        return new PublicApi(apiClient);
    }
}