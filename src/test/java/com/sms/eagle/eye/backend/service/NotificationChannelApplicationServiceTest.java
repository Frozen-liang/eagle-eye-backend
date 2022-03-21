package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

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
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
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
    private final ChannelPageResponse channelPageResponse = mock(ChannelPageResponse.class);

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

    /**
     * {@link NotificationChannelApplicationServiceImpl#getList}.
     *
     * <p>获取通道实例列表
     */
    @Test
    @DisplayName("Test the getList method in the NotificationChannelApplication Service")
    public void getList_test() {
        // mock
        List<ChannelListResponse> list = mock(List.class);
        when(notificationChannelService.getList()).thenReturn(list);
        // 执行
        List<ChannelListResponse> result = notificationChannelApplicationService.getList();
        // 验证
        assertThat(result).isEqualTo(list);
    }


    /**
     * {@link NotificationChannelApplicationServiceImpl#getPage(NotificationChannelQueryRequest)}.
     *
     * <p>根据 Query参数 分页获取通道实例列表.
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
//        // 执行
//        CustomPage<ChannelPageResponse> result = notificationChannelApplicationService.getPage(QueryRequest);
//        // 验证
//        assertThat(result.getRecords()).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getList()}.
     *
     * <p>获取通道类型.
     */
    @Test
    @DisplayName("Test the getConfigFieldsByType method in the NotificationChannelApplication Service")
    public void getConfigFieldsByType_test() {
        // mock
        List<ChannelFieldResponse> list = mock(List.class);
        when(channel.getConfigFields(TYPE)).thenReturn(list);
        // 验证
        assertThat(notificationChannelApplicationService.getConfigFieldsByType(TYPE)).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getInputFieldsByType(Integer)}.
     *
     * <p>根据 Type参数获取通道类型获取配置表单.
     */
    @Test
    @DisplayName("Test the getInputFieldsByType method in the NotificationChannelApplication Service")
    public void getInputFieldsByType_test() {
        // mock
        List<ChannelFieldResponse> list = mock(List.class);
        when(channel.getTaskInputFields(TYPE)).thenReturn(list);
        // 验证
        assertThat(notificationChannelApplicationService.getInputFieldsByType(TYPE)).isEqualTo(list);
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#getByChannelId(Long)}.
     *
     * <p>根据 channelId参数获取通道实例的配置详情.
     */
    @Test
    @DisplayName("Test the getByChannelId method in the NotificationChannelApplication Service")
    public void getByChannelId_test() {
//        //请求构建对象
//        ChannelFieldWithValueResponse valueResponse = ChannelFieldWithValueResponse.builder().value(VAULE).build();
//        // mock
//        NotificationChannelEntity entity = NotificationChannelEntity.builder().id(ID).build();
//        when(notificationChannelService.getEntityById(ID)).thenReturn(entity);
//        List<ChannelFieldWithValueResponse> config = List.of(valueResponse);
//        ChannelDetailResponse response = mock(ChannelDetailResponse.class);
//        doNothing().when(response).setConfig(config);
//        // 执行
//        ChannelDetailResponse result = notificationChannelApplicationService.getByChannelId(ID);
//        // 验证
//        assertThat(result).isNotNull();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#addChannel(NotificationChannelRequest)}.
     *
     * <p>根据 NotificationChannelRequest参数添加通道实例.
     */
    @Test
    @DisplayName("Test the addChannel method in the NotificationChannelApplication Service")
    public void addChannel_test() {
        // mock
        when(configMetadataResolver.checkAndEncrypt(any(), any())).thenReturn(VAULE.toString());
        doNothing().when(channelRequest).setConfig(CONFIG);
        doNothing().when(notificationChannelService).saveFromRequest(channelRequest);
        // 执行
        boolean result = notificationChannelApplicationService.addChannel(channelRequest);
        // 验证
        assertThat(result).isTrue();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#updateChannel(NotificationChannelRequest)}.
     *
     * <p>根据 NotificationChannelRequest参数编辑通道实例.
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
        // 执行
        boolean result = notificationChannelApplicationService.updateChannel(channelRequest);
        // 验证
        assertThat(result).isTrue();
    }

    /**
     * {@link NotificationChannelApplicationServiceImpl#removeChannel(Long)}.
     *
     * <p>根据 channelId参数删除通道实例.
     */
    @Test
    @DisplayName("Test the removeChannel method in the NotificationChannelApplication Service")
    public void removeChannel_test() {
        doNothing().when(notificationChannelService).deleteEntity(ID);
        // 执行
        boolean result = notificationChannelApplicationService.removeChannel(ID);
        // 验证
        assertThat(result).isTrue();
    }

}
