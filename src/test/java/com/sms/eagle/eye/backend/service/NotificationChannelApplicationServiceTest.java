package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.convert.MetadataFieldConverter;
import com.sms.eagle.eye.backend.convert.NotificationChannelConverter;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationChannelService;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import com.sms.eagle.eye.backend.service.impl.NotificationChannelApplicationServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.BeanUtils;

public class NotificationChannelApplicationServiceTest {

    private final Channel channel = mock(Channel.class);
    private final NotificationChannelConverter Converter = mock(NotificationChannelConverter.class);
    private final MetadataFieldConverter metadataFieldConverter = mock(MetadataFieldConverter.class);
    private final ConfigMetadataResolver configMetadataResolver = mock(ConfigMetadataResolver.class);
    private final ConfigMetadataConverter configMetadataConverter = mock(ConfigMetadataConverter.class);
    private final NotificationChannelService notificationChannelService = mock(NotificationChannelService.class);
    private final ChannelDetailResponse channelDetailResponse = mock(ChannelDetailResponse.class);

    private final NotificationChannelApplicationService notificationChannelApplicationService
            = spy(new NotificationChannelApplicationServiceImpl(channel, Converter, metadataFieldConverter,
            configMetadataConverter, configMetadataResolver, notificationChannelService));
    private final NotificationChannelQueryRequest request = mock(NotificationChannelQueryRequest.class);
    private final NotificationChannelRequest channelRequest = mock(NotificationChannelRequest.class);
    private final ConfigMetadata configMetadata = mock(ConfigMetadata.class);
    private final NotificationChannelEntity notificationChannelEntity = mock(NotificationChannelEntity.class);

    private static final Integer TYPE = 1;
    private static final Long ID = 1L;
    private static final String CONFIG = "CONFIG";
    private static final Object VAULE = Object.class;
    private static final MockedStatic<BeanUtils> BEAN_UTILS_MOCKED_STATIC;

    static {
        BEAN_UTILS_MOCKED_STATIC = mockStatic(BeanUtils.class);
    }

    @AfterAll
    private static void close() {
        BEAN_UTILS_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the getList method in the NotificationChannelApplication Service")
    public void getList_test() {
        when(notificationChannelService.getList()).thenReturn(Collections.emptyList());
        assertThat(notificationChannelApplicationService.getList()).isNotNull();
    }

    @Test
    @DisplayName("Test the getPage method in the NotificationChannelApplication Service")
    public void getPage_test() {
        ChannelPageResponse channelPageResponse = Converter.toResponse(notificationChannelEntity);
        when(notificationChannelService.getPage(request)).thenReturn(new Page<>());
        assertThat(notificationChannelApplicationService.getPage(request)).isNotNull();
    }

    @Test
    @DisplayName("Test the getConfigFieldsByType method in the NotificationChannelApplication Service")
    public void getConfigFieldsByType_test() {
        when(channel.getConfigFields(TYPE)).thenReturn(Collections.emptyList());
        assertThat(notificationChannelApplicationService.getInputFieldsByType(TYPE)).isNotNull();
    }

    @Test
    @DisplayName("Test the getInputFieldsByType method in the NotificationChannelApplication Service")
    public void getInputFieldsByType_test() {
        when(channel.getTaskInputFields(TYPE)).thenReturn(Collections.emptyList());
        assertThat(notificationChannelApplicationService.getInputFieldsByType(TYPE)).isNotNull();
    }

    /**
     * TODO.
     */
    @Test
    @DisplayName("Test the getByChannelId method in the NotificationChannelApplication Service")
    public void getByChannelId_test() {
        when(notificationChannelService.getEntityById(ID)).thenReturn(notificationChannelEntity);
        when(Converter.toDetailResponse(notificationChannelEntity)).thenReturn(channelDetailResponse);
        assertThat(notificationChannelApplicationService.getByChannelId(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the addChannel method in the NotificationChannelApplication Service")
    public void addChannel_test() {
        when(configMetadataResolver.checkAndEncrypt(any(), any())).thenReturn(VAULE.toString());
        doNothing().when(channelRequest).setConfig(CONFIG);
        doNothing().when(notificationChannelService).saveFromRequest(channelRequest);
        assertThat(notificationChannelApplicationService.addChannel(channelRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the updateChannel method in the NotificationChannelApplication Service")
    public void updateChannel_test() {
        List<ChannelFieldResponse> fields = List.of(ChannelFieldResponse.builder().build());
        when(channelRequest.getId()).thenReturn(ID);
        when(notificationChannelService.getEntityById(ID)).thenReturn(notificationChannelEntity);
        when(notificationChannelEntity.getType()).thenReturn(TYPE);
        doReturn(fields).when(notificationChannelApplicationService).getConfigFieldsByType(TYPE);
        when(configMetadataResolver.checkAndEncrypt(any(), any())).thenReturn(VAULE.toString());
        doNothing().when(channelRequest).setConfig(CONFIG);
        doNothing().when(notificationChannelService).updateFromRequest(channelRequest);
        assertThat(notificationChannelApplicationService.updateChannel(channelRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the removeChannel method in the NotificationChannelApplication Service")
    public void removeChannel_test() {
        doNothing().when(notificationChannelService).deleteEntity(ID);
        assertThat(notificationChannelApplicationService.removeChannel(ID)).isTrue();
    }

    private List<ChannelFieldWithValueResponse> getFieldValueResponse(NotificationChannelEntity entity) {
        return notificationChannelApplicationService.getConfigFieldsByType(entity.getType()).stream()
                .map(field -> {
                    ConfigMetadata metadata = configMetadataConverter.fromChannelField(field);
                    Map<String, Object> configMap = configMetadataResolver.convertConfigToMap(entity.getConfig());
                    Object value = configMetadataResolver.decryptToFrontendValue(metadata, configMap);
                    return metadataFieldConverter.toChannelValueResponse(field, value);
                })
                .collect(Collectors.toList());
    }
}
