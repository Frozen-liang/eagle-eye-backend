package com.sms.eagle.eye.backend.wecom.config;

import com.sms.eagle.eye.backend.wecom.context.WeComContextHolder;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeComConfiguration {

    public static final String WECOM_CACHE = "wecom";

    @ConditionalOnMissingBean(CacheManager.class)
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setAllowNullValues(true);
        concurrentMapCacheManager.setCacheNames(Collections.singletonList(WECOM_CACHE));
        return concurrentMapCacheManager;
    }

    @Bean
    public WeComContextHolder weChatContextHolder(WeComManager weComManager) {
        WeComContextHolder.setWeChatManager(weComManager);
        return new WeComContextHolder();
    }
}