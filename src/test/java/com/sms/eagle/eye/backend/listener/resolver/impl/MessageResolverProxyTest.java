package com.sms.eagle.eye.backend.listener.resolver.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

public class MessageResolverProxyTest {

    private static final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private static final MessageResolverProxy PROXY = spy(new MessageResolverProxy());

    @BeforeAll
    public static void init() {
        PROXY.setApplicationContext(applicationContext);
    }

    @Test
    void resolve_test_1() {

    }
}