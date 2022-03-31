package com.sms.eagle.eye.backend.wecom.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sms.eagle.eye.backend.wecom.client.decoder.CustomJacksonDecoder;
import feign.Response;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

public class CustomJacksonDecoderTest {

    private final CustomJacksonDecoder decoder = spy(CustomJacksonDecoder.class);

    /**
     * {@link CustomJacksonDecoder#decode(Response, Type)}.
     *
     * <p>情况1: 无异常
     */
    @Test
    public void decode() {
        // request
        Response response = mock(Response.class);
        Type type = mock(Type.class);
    }
}
