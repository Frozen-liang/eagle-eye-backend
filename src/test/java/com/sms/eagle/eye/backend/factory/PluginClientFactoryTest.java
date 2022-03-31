package com.sms.eagle.eye.backend.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

public class PluginClientFactoryTest {

    private final PluginClientFactory factory = spy(PluginClientFactory.class);

    /**
     * {@link PluginClientFactory#getClient(String)}
     */
    @Test
    void getClient_test_1() {
        String target = "127.0.0.1";
        try (MockedConstruction<PluginClient> mocked = mockConstruction(PluginClient.class,
            (mock, context) -> {
            })) {
            PluginClient client = factory.getClient(target);
            assertThat(client).isNotNull();
        }
    }

    /**
     * {@link PluginClientFactory#removeClient(String)}
     */
    @Test
    void removeClient_test_1() {
        String target = "127.0.0.1";
        try (MockedConstruction<PluginClient> mocked = mockConstruction(PluginClient.class,
            (mock, context) -> {
                doNothing().when(mock).shutdown();
            })) {
            factory.getClient(target);
            factory.removeClient(target);
        }
    }
}