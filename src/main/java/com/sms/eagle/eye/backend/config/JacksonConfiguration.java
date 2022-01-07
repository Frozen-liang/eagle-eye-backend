package com.sms.eagle.eye.backend.config;

import com.fasterxml.jackson.databind.Module;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Module protobufModule() {
        return new ProtobufModule();
    }
}