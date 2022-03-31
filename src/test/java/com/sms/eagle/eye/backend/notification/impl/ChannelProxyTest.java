package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_TYPE_IS_NOT_CORRECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

public class ChannelProxyTest {

    private static final ApplicationContext APPLICATION_CONTEXT = mock(ApplicationContext.class);
    private static final Integer CHANNEL_TYPE_VALUE = 1;
    private static final Integer UN_EXIST_CHANNEL_TYPE_VALUE = 2;
    private static final Channel CHANNEL = mock(Channel.class);
    private static final ChannelProxy CHANNEL_PROXY = spy(new ChannelProxy());
    private static final String EXCEPTION_CODE = "code";


    private static final MockedStatic<AnnotationUtils> ANNOTATION_UTILS_MOCKED_STATIC
        = mockStatic(AnnotationUtils.class);

    @AfterAll
    public static void close() {
        ANNOTATION_UTILS_MOCKED_STATIC.close();
    }

    @BeforeAll
    public static void init() {
        // mock static
        Notification notification = mock(Notification.class);
        ANNOTATION_UTILS_MOCKED_STATIC.when(() -> AnnotationUtils
                .findAnnotation(CHANNEL.getClass(), Notification.class)).thenReturn(notification);
        // mock notification.type()
        NotificationChannelType channelType = mock(NotificationChannelType.class);
        when(notification.type()).thenReturn(channelType);
        // mock channelType.getValue()
        when(channelType.getValue()).thenReturn(CHANNEL_TYPE_VALUE);
        // mock applicationContext.getBeansOfType
        Map<String, Channel> channelMap = new HashMap<>();
        channelMap.put("CHANNEL", CHANNEL);
        when(APPLICATION_CONTEXT.getBeansOfType(Channel.class)).thenReturn(channelMap);
        CHANNEL_PROXY.setApplicationContext(APPLICATION_CONTEXT);
        CHANNEL_PROXY.afterPropertiesSet();
    }

    /**
     * {@link ChannelProxy#getConfigFields(Integer)}
     *
     * <p>情形1：输入的类型存在
     */
    @Test
    void getConfigFields_test_1() {
        // mock channel.getConfigFields()
        List<ChannelFieldResponse> channelFieldResponses = mock(List.class);
        when(CHANNEL.getConfigFields(CHANNEL_TYPE_VALUE)).thenReturn(channelFieldResponses);
        // invoke
        List<ChannelFieldResponse> result = CHANNEL_PROXY.getConfigFields(CHANNEL_TYPE_VALUE);
        // assert
        assertThat(result).isEqualTo(channelFieldResponses);
    }

    /**
     * {@link ChannelProxy#getConfigFields(Integer)}
     *
     * <p>情形2：输入的类型不存在
     */
    @Test
    void getConfigFields_test_2() {
        // invoke and assert
        assertThatThrownBy(() -> CHANNEL_PROXY.getConfigFields(UN_EXIST_CHANNEL_TYPE_VALUE))
            .isInstanceOf(Exception.class)
            .extracting(EXCEPTION_CODE).isEqualTo(CHANNEL_TYPE_IS_NOT_CORRECT.getCode());
    }

    /**
     * {@link ChannelProxy#getTaskInputFields(Integer)}
     *
     * <p>情形1：输入的类型存在
     */
    @Test
    void getTaskInputFields_test_1() {
        // mock channel.getTaskInputFields()
        List<ChannelFieldResponse> channelFieldResponses = mock(List.class);
        when(CHANNEL.getTaskInputFields(CHANNEL_TYPE_VALUE)).thenReturn(channelFieldResponses);
        // invoke
        List<ChannelFieldResponse> result = CHANNEL_PROXY.getTaskInputFields(CHANNEL_TYPE_VALUE);
        // assert
        assertThat(result).isEqualTo(channelFieldResponses);
    }

    /**
     * {@link ChannelProxy#getTaskInputFields(Integer)}
     *
     * <p>情形2：输入的类型不存在
     */
    @Test
    void getTaskInputFields_test_2() {
        // invoke and assert
        assertThatThrownBy(() -> CHANNEL_PROXY.getTaskInputFields(UN_EXIST_CHANNEL_TYPE_VALUE))
            .isInstanceOf(Exception.class)
            .extracting(EXCEPTION_CODE).isEqualTo(CHANNEL_TYPE_IS_NOT_CORRECT.getCode());
    }

    /**
     * {@link ChannelProxy#notify(NotificationEvent)}
     *
     * <p>情形1：输入的类型存在
     */
    @Test
    void notify_test_1() {
        // event.getChannelType()
        NotificationEvent event = mock(NotificationEvent.class);
        when(event.getChannelType()).thenReturn(CHANNEL_TYPE_VALUE);
        // mock notify()
        doReturn(true).when(CHANNEL).notify(event);
        // invoke
       boolean result = CHANNEL_PROXY.notify(event);
        // assert
        assertThat(result).isEqualTo(true);
    }

    /**
     * {@link ChannelProxy#notify(NotificationEvent)}
     *
     * <p>情形2：输入的类型不存在
     */
    @Test
    void notify_test_2() {
        // event.getChannelType()
        NotificationEvent event = mock(NotificationEvent.class);
        when(event.getChannelType()).thenReturn(UN_EXIST_CHANNEL_TYPE_VALUE);
        // invoke and assert
        assertThatThrownBy(() -> CHANNEL_PROXY.notify(event))
            .isInstanceOf(Exception.class)
            .extracting(EXCEPTION_CODE).isEqualTo(CHANNEL_TYPE_IS_NOT_CORRECT.getCode());
    }
}