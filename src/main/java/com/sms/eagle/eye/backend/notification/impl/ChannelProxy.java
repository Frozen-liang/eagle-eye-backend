package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_TYPE_IS_NOT_CORRECT;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
@Qualifier(ChannelProxy.CHANNEL_PROXY)
public class ChannelProxy implements Channel, ApplicationContextAware, InitializingBean {

    public static final String CHANNEL_PROXY = "channelProxy";

    private ApplicationContext applicationContext;
    private Map<Integer, Channel> channelMap = new HashMap<>();

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        Optional<Channel> channelOptional = Optional.ofNullable(channelMap.get(type));
        if (channelOptional.isPresent()) {
            return channelOptional.get().getConfigFields(type);
        }
        throw new EagleEyeException(CHANNEL_TYPE_IS_NOT_CORRECT);
    }

    @Override
    public List<ChannelFieldResponse> getTaskInputFields(Integer type) {
        Optional<Channel> channelOptional = Optional.ofNullable(channelMap.get(type));
        if (channelOptional.isPresent()) {
            return channelOptional.get().getTaskInputFields(type);
        }
        throw new EagleEyeException(CHANNEL_TYPE_IS_NOT_CORRECT);
    }

    @Override
    public boolean notify(NotificationEvent event) {
        Optional<Channel> channelOptional = Optional.ofNullable(channelMap.get(event.getChannelType()));
        if (channelOptional.isPresent()) {
            return channelOptional.get().notify(event);
        }
        throw new EagleEyeException(CHANNEL_TYPE_IS_NOT_CORRECT);
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Channel> map = applicationContext.getBeansOfType(Channel.class);
        channelMap = map.values().stream()
            .filter(channel -> Objects.nonNull(
                AnnotationUtils.findAnnotation(channel.getClass(), Notification.class)))
            .collect(Collectors.toMap(channel -> Objects
                .requireNonNull(AnnotationUtils.findAnnotation(
                    channel.getClass(), Notification.class)).type().getValue(), Function.identity()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}