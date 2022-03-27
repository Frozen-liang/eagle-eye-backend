package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.common.Constant.NERKO_TOKEN_CACHE_KEY;
import static com.sms.eagle.eye.backend.common.Constant.PROJECT_CACHE_KEY;
import static com.sms.eagle.eye.backend.common.Constant.USER_CACHE_KEY;
import static com.sms.eagle.eye.backend.common.Constant.WECOM_TOKEN_CACHE_KEY;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import lombok.Getter;

@Getter
public enum SystemCache {
    NERKO_TOKEN_CACHE(NERKO_TOKEN_CACHE_KEY, defaultCache()),
    WECOM_TOKEN_CACHE(WECOM_TOKEN_CACHE_KEY, defaultCache()),
    PROJECT_CACHE(PROJECT_CACHE_KEY, resourceCache()),
    USER_CACHE(USER_CACHE_KEY, resourceCache());


    private static Cache<Object, Object> defaultCache() {
        return Caffeine.newBuilder().build();
    }

    private static Cache<Object, Object> resourceCache() {
        return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
    }

    private final String key;
    private final Cache<Object, Object> cache;

    SystemCache(String key, Cache<Object, Object> cache) {
        this.key = key;
        this.cache = cache;
    }
}
