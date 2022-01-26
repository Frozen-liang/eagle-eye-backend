package com.sms.eagle.eye.backend.listener;

import com.sms.eagle.eye.backend.listener.resolver.MessageResolver;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AwsMessageListener  {

    private final MessageResolver messageResolver;

    public AwsMessageListener(@Qualifier("messageResolverProxy") MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    @SqsListener(value = "Monitor.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String payload, @Header("MessageGroupId") String messageGroup) {
        log.info("MessageGroupId: {}, Payload: {}", messageGroup, payload);
        messageResolver.resolve(payload, messageGroup);
    }
}