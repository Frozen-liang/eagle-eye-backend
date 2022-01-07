package com.sms.eagle.eye.backend.factory;

import com.sms.eagle.eye.plugin.v1.PluginServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PluginClient {

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

}