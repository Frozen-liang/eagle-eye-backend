package com.sms.eagle.eye.backend.config;

import com.sms.eagle.eye.backend.interceptor.UserinfoInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;
    private final UserinfoInterceptor userinfoInterceptor;

    public WebMvcConfig(CorsProperties corsProperties,
        UserinfoInterceptor userinfoInterceptor) {
        this.corsProperties = corsProperties;
        this.userinfoInterceptor = userinfoInterceptor;
    }

    /**
     * Enable PathPattern in WebMvc.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(PathPatternParser.defaultInstance);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsProperties.getMapping())
            .allowedOriginPatterns(corsProperties.getOrigins())
            .allowedMethods(corsProperties.getMethods())
            .maxAge(corsProperties.getMaxAge())
            .allowCredentials(corsProperties.getAllowCredentials());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userinfoInterceptor).addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}