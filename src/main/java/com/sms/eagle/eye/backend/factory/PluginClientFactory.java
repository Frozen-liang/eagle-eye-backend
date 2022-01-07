package com.sms.eagle.eye.backend.factory;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PluginClientFactory {

    private final Map<String, PluginClient> map = new HashMap<>();

    public PluginClient getClient(String target) {
        return map.computeIfAbsent(target, PluginClient::new);
    }
}