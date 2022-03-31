package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelFieldConverterTest {

    private final ChannelFieldConverterImpl converter = new ChannelFieldConverterImpl();
    private final ChannelFieldResponse channelFieldResponse = mock(ChannelFieldResponse.class);

    private final Object VAULE = Object.class;
    private final String KEY = "KEY";

    @Test
    void fillValueToResponse_test1() {
        // 验证
        assertThat(converter.fillValueToResponse(null, null)).isNull();
    }

    @Test
    void fillValueToResponse_test2() {
        // mock
        when(channelFieldResponse.getKey()).thenReturn(KEY);
        // 执行
        ChannelFieldWithValueResponse response = converter.fillValueToResponse(channelFieldResponse, null);
        // 验证
        assertThat(response).isNotNull();
        assertThat(response.getKey()).isEqualTo(KEY);
    }

    @Test
    void fillValueToResponse_test3() {
        // 执行
        Object result = converter.fillValueToResponse(null, VAULE).getValue();
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(VAULE);
    }
}
