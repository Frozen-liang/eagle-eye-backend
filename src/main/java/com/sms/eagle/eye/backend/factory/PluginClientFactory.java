package com.sms.eagle.eye.backend.factory;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PluginClientFactory {

    private final Map<String, PluginClient> MAP = new HashMap<>();

    public PluginClient getClient(String target) {
        return MAP.computeIfAbsent(target, PluginClient::new);
    }
}