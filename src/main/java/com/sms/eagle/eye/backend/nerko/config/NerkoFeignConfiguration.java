package com.sms.eagle.eye.backend.nerko.config;

import com.sms.eagle.eye.backend.nerko.client.decoder.NerkoJacksonDecoder;
import com.sms.eagle.eye.backend.nerko.interceptor.NerkoInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;

public class NerkoFeignConfiguration {

    @Bean
    public Decoder decoder() {
        return new NerkoJacksonDecoder();
    }

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Bean
    public NerkoInterceptor nerkoInterceptor() {
        return new NerkoInterceptor();
    }
}