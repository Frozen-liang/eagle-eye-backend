package com.sms.eagle.eye.backend.factory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PluginClientFactory {

    private final Map<String, PluginClient> map = new ConcurrentHashMap<>();

    public PluginClient getClient(String target) {
        return map.computeIfAbsent(target, PluginClient::new);
    }

    public void removeClient(String target) {
        Optional<PluginClient> optional = Optional.ofNullable(map.get(target));
        optional.ifPresent(pluginClient -> {
            pluginClient.shutdown();
            map.remove(target);
        });
    }
}