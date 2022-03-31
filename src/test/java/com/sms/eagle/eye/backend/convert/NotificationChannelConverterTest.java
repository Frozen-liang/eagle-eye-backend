package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotificationChannelConverterTest {

    private final NotificationChannelConverter converter = new NotificationChannelConverterImpl();
    private final NotificationChannelEntity channelEntity = mock(NotificationChannelEntity.class);
    private final NotificationChannelRequest request = mock(NotificationChannelRequest.class);

    private final String NAME = "NAME";
    private final Integer TYPE = 1;
    private final String EMAIL = "Email";

    @Test
    void toResponse_test1() {
        assertThat(converter.toResponse(null)).isNull();
    }

    /**
     * {@link NotificationChannelConverter#toResponse(NotificationChannelEntity)}.
     *
     * <p>当type 为1时 代表为Email形式
     */
    @Test
    void toResponse_test2() {
        // mock
        when(channelEntity.getName()).thenReturn(NAME);
        when(channelEntity.getType()).thenReturn(TYPE);
        // 执行
        ChannelPageResponse response = converter.toResponse(channelEntity);
        // 验证
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getType()).isEqualTo(EMAIL);
    }

    @Test
    void toDetailResponse_test1() {
        assertThat(converter.toDetailResponse(null)).isNull();
    }

    @Test
    void toDetailResponse_test2() {
        // mock
        when(channelEntity.getName()).thenReturn(NAME);
        // 执行
        ChannelDetailResponse response = converter.toDetailResponse(channelEntity);
        // 验证
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(NAME);
    }

    @Test
    void toListResponse_test1() {
        assertThat(converter.toListResponse(null)).isNull();
    }

    @Test
    void toListResponse_test2() {
        // mock
        when(channelEntity.getName()).thenReturn(NAME);
        // 执行
        ChannelListResponse response = converter.toListResponse(channelEntity);
        // 验证
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(NAME);
    }

    @Test
    void toEntity_test1() {
        assertThat(converter.toEntity(null)).isNull();
    }

    @Test
    void toEntity_test2() {
        // mock
        when(request.getName()).thenReturn(NAME);
        // 执行
        NotificationChannelEntity entity = converter.toEntity(request);
        // 验证
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(NAME);
    }
}
