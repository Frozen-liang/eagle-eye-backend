package com.sms.eagle.eye.backend.wecom;

import com.sms.eagle.eye.backend.wecom.client.decoder.CustomJacksonDecoder;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import feign.Response;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class CustomJacksonDecoderTest {

    private final CustomJacksonDecoder decoder = new CustomJacksonDecoder();


    /**
     * {@link CustomJacksonDecoder#decode(Response, Type)}.
     *
     * 情况1: 无异常
     */
    @Test
    public void decode(){

    }
}
