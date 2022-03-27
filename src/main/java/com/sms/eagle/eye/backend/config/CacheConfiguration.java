package com.sms.eagle.eye.backend.config;

import com.sms.eagle.eye.backend.common.enums.SystemCache;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> cacheList = new ArrayList<>();
        for (SystemCache systemCache : SystemCache.values()) {
            cacheList.add(new CaffeineCache(systemCache.getKey(), systemCache.getCache()));
        }
        cacheManager.setCaches(cacheList);
        return cacheManager;
    }
}