package com.sms.eagle.eye.backend.nerko;

import com.sms.eagle.eye.backend.nerko.client.decoder.NerkoJacksonDecoder;
import feign.Request;
import feign.jackson.JacksonDecoder;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import feign.Response;
import java.io.IOException;

public class NerkoJacksonDecoderTest {

    JacksonDecoder decoder = mock(JacksonDecoder.class);
    NerkoJacksonDecoder nerkoJacksonDecoder = new NerkoJacksonDecoder();

    @Test
    public void decode() throws IOException {
        // 请求构建对象
        Object OBJECT = Object.class;
        int STATUS = 1;
        // mock
        Request request = mock(Request.class);
        Response response = Response.builder().status(STATUS).request(request).build();
//        Response response = mock(Response.class);
        when(decoder.decode(any(), any())).thenReturn(OBJECT);
        // 执行
//        Object result = nerkoJacksonDecoder.decode(any(Response.class), any(Type.class));
        // 验证
//        assertThat(result).isNotNull();
//        assertThat(result).isEqualTo(OBJECT);
    }
}
