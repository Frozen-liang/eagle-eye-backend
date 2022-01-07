package com.sms.eagle.eye.backend.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(NerkoOAuth2Properties.class)
public class NerkoOAuth2Configuration {

    public static final String ACCESS_TOKEN = "accessToken";

    private final NerkoOAuth2Properties nerkoOAuth2Properties;

    public NerkoOAuth2Configuration(NerkoOAuth2Properties nerkoOAuth2Properties) {
        this.nerkoOAuth2Properties = nerkoOAuth2Properties;
    }

    @Bean
    @Qualifier(ACCESS_TOKEN)
    public RestTemplate accessTokenRestTemplate() {
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(15))
            .setReadTimeout(Duration.ofSeconds(5))
            .additionalInterceptors(new BasicAuthenticationInterceptor(
                nerkoOAuth2Properties.getClientId(), nerkoOAuth2Properties.getClientSecret()))
            .build();
    }

}