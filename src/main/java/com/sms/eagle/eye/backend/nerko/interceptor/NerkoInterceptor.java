package com.sms.eagle.eye.backend.nerko.interceptor;

import com.sms.eagle.eye.backend.context.AccessTokenContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NerkoInterceptor implements RequestInterceptor {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_FORMAT = "Bearer %s";

    /**
     * 统一处理AccessToken.
     */
    @Override
    public void apply(RequestTemplate template) {
        String token = AccessTokenContextHolder.getTokenService().getAccessTokenByClientCredential();
        template.header(AUTH_HEADER, String.format(AUTH_FORMAT, token));
    }
}
