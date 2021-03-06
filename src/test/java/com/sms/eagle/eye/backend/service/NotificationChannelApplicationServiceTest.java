package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.service.impl.NotificationChannelApplicationServiceImpl;
import java.util.List;
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

    private final NotificationChannelApplicationService notificationChannelApplicationService
        = spy(new NotificationChannelApplicationServiceImpl(channel, Converter, metadataFieldConverter,
        configMetadataConverter, configMetadataResolver, notificationChannelService));
    private final NotificationChannelRequest channelRequest = mock(NotificationChannelRequest.class);
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

    /**
     * {@link NotificationChannelApplicationServiceImpl#getList}.
     *
     * <p>????????????????????????
     */
    @Test
    @DisplayName("Test the getList method in the NotificationChannelApplication Service")
    public void getList_test() {
        // mock
        List<ChannelListResponse> list = mock(List.class);
        when(notificationChannelService.getList()).thenReturn(list);
        // ??????
        List<ChannelListResponse> result = notificationChannelApplicationService.getList();
        // ??????
        assertThat(result).isEqualTo(list);
    }


    /**
     * {@link NotificationChannelApplicationServiceImpl#getPage(NotificationChannelQueryRequest)}.
     *
     * <p>?????? Query?????? ??????????????????????????????.
     */
    @Test
    @DisplayName("Test the getPage method in the NotificationChannelApplication Service")
    public void getPage_test() {
//        // mock
//
//        NotificationChannelQueryRequest QueryRequest = mock(NotificationChannelQueryRequest.class);
//        IPage<NotificationChannelEntity> entityIPage = mock(IPage.class);
//        when(notificationChannelService.getPage(QueryRequest)).thenReturn(entityIPage);
//
//        NotificationChannelConverter converter = mock(NotificationChannelConverter.class);
//        NotificationChannelEntity channelEntity = mock(NotificationChannelEntity.class);
//        when(converter.toResponse(channelEntity)).thenReturn(channelPageResponse);
//
//        List<ChannelPageResponse> list = mock(List.class);
//        IPage<ChannelPageResponse> iPage = mock(IPage.class);
//        when(entityIPage.convert(any())).thenReturn(iPage);
//        doReturn(list).when(iPage).getRecords();
//        // ??????
//        CustomPage<ChannelPageResponse> result = notificationChannelApplicationService.getPage(QueryRequest);
//        // ??????
//        assertThat(result.getRecords()).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getList()}.
     *
     * <p>??????????????????.
     */
    @Test
    @DisplayName("Test the getConfigFieldsByType method in the NotificationChannelApplication Service")
    public void getConfigFieldsByType_test() {
        // mock
        List<ChannelFieldResponse> list = mock(List.class);
        when(channel.getConfigFields(TYPE)).thenReturn(list);
        // ??????
        assertThat(notificationChannelApplicationService.getConfigFieldsByType(TYPE)).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getInputFieldsByType(Integer)}.
     *
     * <p>?????? Type??????????????????????????????????????????.
     */
    @Test
    @DisplayName("Test the getInputFieldsByType method in the NotificationChannelApplication Service")
    public void getInputFieldsByType_test() {
        // mock
        List<ChannelFieldResponse> list = mock(List.class);
        when(channel.getTaskInputFields(TYPE)).thenReturn(list);
        // ??????
        assertThat(notificationChannelApplicationService.getInputFieldsByType(TYPE)).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getByChannelId(Long)}.
     *
     * <p>?????? channelId???????????????????????????????????????.
     */
    @Test
    @DisplayName("Test the getByChannelId method in the NotificationChannelApplication Service")
    public void getByChannelId_test() {
//        //??????????????????
//        ChannelFieldWithValueResponse valueResponse = ChannelFieldWithValueResponse.builder().value(VAULE).build();
//        // mock
//        NotificationChannelEntity entity = NotificationChannelEntity.builder().id(ID).build();
//        when(notificationChannelService.getEntityById(ID)).thenReturn(entity);
//        List<ChannelFieldWithValueResponse> config = List.of(valueResponse);
//        ChannelDetailResponse response = mock(ChannelDetailResponse.class);
//        doNothing().when(response).setConfig(config);
//        // ??????
//        ChannelDetailResponse result = notificationChannelApplicationService.getByChannelId(ID);
//        // ??????
//        assertThat(result).isNotNull();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#addChannel(NotificationChannelRequest)}.
     *
     * <p>?????? NotificationChannelRequest????????????????????????.
     */
    @Test
    @DisplayName("Test the addChannel method in the NotificationChannelApplication Service")
    public void addChannel_test() {
        // mock
        when(configMetadataResolver.checkAndEncrypt(any(), any())).thenReturn(VAULE.toString());
        doNothing().when(channelRequest).setConfig(CONFIG);
        doNothing().when(notificationChannelService).saveFromRequest(channelRequest);
        // ??????
        boolean result = notificationChannelApplicationService.addChannel(channelRequest);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#updateChannel(NotificationChannelRequest)}.
     *
     * <p>?????? NotificationChannelRequest????????????????????????.
     */
    @Test
    @DisplayName("Test the updateChannel method in the NotificationChannelApplication Service")
    public void updateChannel_test() {
        when(channelRequest.getId()).thenReturn(ID);
        when(notificationChannelService.getEntityById(ID)).thenReturn(notificationChannelEntity);
        when(notificationChannelEntity.getType()).thenReturn(TYPE);
        List<ConfigMetadata> metadataList = mock(List.class);
        when(configMetadataResolver.checkAndEncrypt(metadataList, CONFIG)).thenReturn(VAULE.toString());
        doNothing().when(channelRequest).setConfig(CONFIG);
        doNothing().when(notificationChannelService).updateFromRequest(channelRequest);
        // ??????
        boolean result = notificationChannelApplicationService.updateChannel(channelRequest);
        // ??????
        assertThat(result).isTrue();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#removeChannel(Long)}.
     *
     * <p>?????? channelId????????????????????????.
     */
    @Test
    @DisplayName("Test the removeChannel method in the NotificationChannelApplication Service")
    public void removeChannel_test() {
        doNothing().when(notificationChannelService).deleteEntity(ID);
        // ??????
        boolean result = notificationChannelApplicationService.removeChannel(ID);
        // ??????
        assertThat(result).isTrue();
    }

}
