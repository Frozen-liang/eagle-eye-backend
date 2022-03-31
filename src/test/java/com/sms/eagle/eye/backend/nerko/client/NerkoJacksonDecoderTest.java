package com.sms.eagle.eye.backend.nerko.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sms.eagle.eye.backend.nerko.client.decoder.NerkoJacksonDecoder;
import feign.Response;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

public class NerkoJacksonDecoderTest {

    NerkoJacksonDecoder nerkoJacksonDecoder = spy(NerkoJacksonDecoder.class);

    /**
     * {@link NerkoJacksonDecoder#decode(Response, Type)}
     */
    @Test
    public void decode() {
        // request
        Response response = mock(Response.class);
        Type type = mock(Type.class);
    }
}
