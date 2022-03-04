package com.sms.eagle.eye.backend.wecom.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;

public abstract class WeComPropertiesContextHolder {

    private static final TransmittableThreadLocal<WeComProperties> propertiesContext = new TransmittableThreadLocal<>();

    public static void restProperties() {
        propertiesContext.remove();
    }

    public static WeComProperties getProperties() {
        return propertiesContext.get();
    }

    public static void setProperties(WeComProperties properties) {
        propertiesContext.set(properties);
    }
}