package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.convert.NotificationChannelConverter;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.mapper.NotificationChannelMapper;
import com.sms.eagle.eye.backend.domain.service.impl.NotificationChannelServiceImpl;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.io.Serializable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class NotificationChannelServiceTest {

    private final NotificationChannelConverter converter = mock(NotificationChannelConverter.class);
    private final NotificationChannelEntity entity = mock(NotificationChannelEntity.class);
    private final NotificationChannelQueryRequest queryRequest = mock(NotificationChannelQueryRequest.class);
    private final NotificationChannelMapper channelMapper = mock(NotificationChannelMapper.class);
    private final NotificationChannelRequest channelRequest = mock(NotificationChannelRequest.class);
    private final NotificationChannelService notificationChannelService =
            spy(new NotificationChannelServiceImpl(converter));

    private static final String VALUE = "VALUE";
    private static final Long ID = 1L;

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);

    static {
        UserInfo userInfo = UserInfo.builder().username("username").nickname("nickname").email("email@test.com")
                .build();
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the getList method in the Notification Channel Service")
    public void getList_test() {

    }

    @Test
    @DisplayName("Test the getPage method in the Notification Channel Service")
    public void getPage_test() {
        IPage<NotificationChannelEntity> result = mock(IPage.class);
        Page<NotificationChannelEntity> page = mock(Page.class);
        when(queryRequest.getPageInfo()).thenReturn(page);
        doReturn(channelMapper).when(notificationChannelService).getBaseMapper();
        when(channelMapper.getPage(page, queryRequest)).thenReturn(result);
        assertThat(notificationChannelService.getPage(queryRequest)).isEqualTo(result);
    }

    @Test
    @DisplayName("Test the getEntityById method in the Notification Channel Service")
    public void getEntityById_test() {
//        doReturn(Boolean.TRUE).when(notificationChannelService).getById(any(Serializable.class));
//        assertThat(notificationChannelService.getEntityById(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the saveFromRequest method in the Notification Channel Service")
    public void saveFromRequest_test() {
        when(converter.toEntity(channelRequest)).thenReturn(entity);
        doNothing().when(entity).setCreator(VALUE);
        doReturn(Boolean.TRUE).when(notificationChannelService).save(entity);
        notificationChannelService.saveFromRequest(channelRequest);
        verify(notificationChannelService).save(any());
    }

    @Test
    @DisplayName("Test the updateFromRequest method in the Notification Channel Service")
    public void updateFromRequest_test() {
        when(converter.toEntity(channelRequest)).thenReturn(entity);
        doReturn(Boolean.TRUE).when(notificationChannelService).updateById(entity);
        notificationChannelService.updateFromRequest(channelRequest);
        verify(notificationChannelService).updateById(entity);
    }

    @Test
    @DisplayName("Test the deleteEntity method in the Notification Channel Service")
    public void deleteEntity_test() {
        doReturn(Boolean.TRUE).when(notificationChannelService).removeById(any(Serializable.class));
        notificationChannelService.deleteEntity(ID);
        verify(notificationChannelService).removeById(ID);
    }
}
