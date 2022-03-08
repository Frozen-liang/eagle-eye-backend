package com.sms.eagle.eye.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ResolversConfiguration {

    @Bean
    public ITemplateResolver stringResolver() {
        return new StringTemplateResolver();
    }
}
