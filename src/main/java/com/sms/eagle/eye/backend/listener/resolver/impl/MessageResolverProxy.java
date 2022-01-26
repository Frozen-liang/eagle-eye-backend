package com.sms.eagle.eye.backend.listener.resolver.impl;

import com.sms.eagle.eye.backend.listener.resolver.AwsMessage;
import com.sms.eagle.eye.backend.listener.resolver.AwsMessageGroup;
import com.sms.eagle.eye.backend.listener.resolver.MessageResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
@Qualifier("messageResolverProxy")
public class MessageResolverProxy implements MessageResolver, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<AwsMessageGroup, MessageResolver> resolverMap = new HashMap<>();

    @Override
    public void resolve(String payload, String group) {
        Optional<AwsMessageGroup> optional = AwsMessageGroup.resolve(group);
        optional.ifPresent(awsMessageGroup -> {
            Optional<MessageResolver> resolverOptional = Optional.ofNullable(resolverMap.get(awsMessageGroup));
            resolverOptional.ifPresent(resolver -> resolver.resolve(payload, group));
        });
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, MessageResolver> map = applicationContext.getBeansOfType(MessageResolver.class);
        resolverMap = map.values().stream()
            .filter(resolver -> Objects.nonNull(
                AnnotationUtils.findAnnotation(resolver.getClass(), AwsMessage.class)))
            .collect(Collectors.toMap(resolver -> Objects
                .requireNonNull(AnnotationUtils.findAnnotation(
                    resolver.getClass(), AwsMessage.class)).group(), Function.identity()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}