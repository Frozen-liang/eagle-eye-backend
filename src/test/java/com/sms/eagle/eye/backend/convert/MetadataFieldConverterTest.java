package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class MetadataFieldConverterTest {

    private final MetadataFieldConverter converter = new MetadataFieldConverterImpl();
    private final ChannelFieldResponse response = mock(ChannelFieldResponse.class);

    private final Object VAULE = Object.class;
    private final String KEY = "KEY";

    @Test
    void toChannelValueResponse_test1() {
        assertThat(converter.toChannelValueResponse(null, null)).isNull();
    }

    @Test
    void toChannelValueResponse_test2() {
        // mock
        when(response.getKey()).thenReturn(KEY);
        // 执行
        ChannelFieldWithValueResponse result = converter.toChannelValueResponse(response, null);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result.getKey()).isEqualTo(KEY);
    }

    @Test
    void toChannelValueResponse_test3() {
        Object value = converter.toChannelValueResponse(null, VAULE).getValue();
        // 验证
        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(VAULE);
    }
}
