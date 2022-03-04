package com.sms.eagle.eye.backend.wecom.config;

import com.sms.eagle.eye.backend.wecom.client.decoder.CustomJacksonDecoder;
import com.sms.eagle.eye.backend.wecom.interceptor.WeComInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;

public class WeComFeignConfiguration {

    @Bean
    public Decoder decoder() {
        return new CustomJacksonDecoder();
    }

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Bean
    public WeComInterceptor weChatInterceptor() {
        return new WeComInterceptor();
    }
}