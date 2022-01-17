package com.sms.eagle.eye.backend.factory;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_CLIENT_SHUTDOWN_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.plugin.v1.PluginServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginClient {

    private static final int TIMEOUT = 10;
    private final ManagedChannel channel;
    private final PluginServiceGrpc.PluginServiceBlockingStub blockingStub;
    private final PluginServiceGrpc.PluginServiceStub asyncStub;
    private final PluginServiceGrpc.PluginServiceFutureStub futureStub;

    public PluginClient(String target) {
        channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        blockingStub = PluginServiceGrpc.newBlockingStub(channel);
        asyncStub = PluginServiceGrpc.newStub(channel);
        futureStub = PluginServiceGrpc.newFutureStub(channel);
    }

    public PluginServiceGrpc.PluginServiceBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public PluginServiceGrpc.PluginServiceStub getAsyncStub() {
        return asyncStub;
    }

    public PluginServiceGrpc.PluginServiceFutureStub getFutureStub() {
        return futureStub;
    }

    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error(PLUGIN_CLIENT_SHUTDOWN_ERROR.getMessage(), exception);
            throw new EagleEyeException(PLUGIN_CLIENT_SHUTDOWN_ERROR);
        }
    }
}