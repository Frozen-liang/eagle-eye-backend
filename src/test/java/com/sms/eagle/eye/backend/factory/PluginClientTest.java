package com.sms.eagle.eye.backend.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.plugin.v1.PluginServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class PluginClientTest {

    private static final String target ="127.0.0.1";
    private static final ManagedChannel channel = mock(ManagedChannel.class);
    private static final MockedStatic<ManagedChannelBuilder> MANAGED_CHANNEL_BUILDER_MOCKED_STATIC
        = mockStatic(ManagedChannelBuilder.class);

    static {
        ManagedChannelBuilder forTargetBuilder = mock(ManagedChannelBuilder.class);
        MANAGED_CHANNEL_BUILDER_MOCKED_STATIC.when(() ->
                ManagedChannelBuilder.forTarget(target)).thenReturn(forTargetBuilder);
        ManagedChannelBuilder usePlaintextBuilder = mock(ManagedChannelBuilder.class);
        when(forTargetBuilder.usePlaintext()).thenReturn(usePlaintextBuilder);
        when(usePlaintextBuilder.build()).thenReturn(channel);
    }

    @AfterAll
    public static void close() {
        MANAGED_CHANNEL_BUILDER_MOCKED_STATIC.close();
    }

    private final PluginClient pluginClient = spy(new PluginClient(target));

    @Test
    void getBlockingStub_test() {
        PluginServiceGrpc.PluginServiceBlockingStub blockingStub = pluginClient.getBlockingStub();
        assertThat(blockingStub).isNotNull();
    }

    @Test
    void getAsyncStub_test() {
        PluginServiceGrpc.PluginServiceStub asyncStub = pluginClient.getAsyncStub();
        assertThat(asyncStub).isNotNull();
    }

    @Test
    void getFutureStub_test() {
        PluginServiceGrpc.PluginServiceFutureStub futureStub = pluginClient.getFutureStub();
        assertThat(futureStub).isNotNull();
    }

    @Test
    void shutdown_test() {
        when(channel.shutdown()).thenThrow(new RuntimeException());
        assertThatThrownBy(pluginClient::shutdown)
            .isInstanceOf(EagleEyeException.class);
    }
}