package com.sms.eagle.eye.backend.listener;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.sms.eagle.eye.backend.listener.resolver.MessageResolver;
import org.junit.jupiter.api.Test;

public class AwsMessageListenerTest {

    private final MessageResolver messageResolver = mock(MessageResolver.class);
    private final AwsMessageListener awsMessageListener = spy(new AwsMessageListener(messageResolver));

    /**
     * {@link AwsMessageListener#receiveMessage(String, String)}
     */
    @Test
    void receiveMessage_test() {
        String payload = "payload";
        String messageGroup = "messageGroup";
        // mock messageResolver.resolve
        doNothing().when(messageResolver).resolve(payload, messageGroup);
        // invoke
        awsMessageListener.receiveMessage(payload, messageGroup);
        // verify
        verify(messageResolver).resolve(payload, messageGroup);
    }
}