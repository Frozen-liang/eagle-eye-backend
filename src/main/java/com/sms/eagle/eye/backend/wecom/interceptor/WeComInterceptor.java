package com.sms.eagle.eye.backend.wecom.interceptor;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComContextHolder;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.ArrayList;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class WeComInterceptor implements RequestInterceptor {

    /**
     * 统一处理AccessToken.
     */
    @Override
    public void apply(RequestTemplate template) {
        if (!template.url().startsWith(WeComClient.GET_TOKEN)) {
            String app = "";
            Collection<String> headerApp = template.headers().get(WeComClient.HEAD_KEY);
            if (CollectionUtils.isNotEmpty(headerApp)) {
                String realApp = new ArrayList<>(headerApp).get(0);
                if (StringUtils.isNotEmpty(realApp)) {
                    app = realApp;
                }
            }

            if (StringUtils.isEmpty(app)) {
                throw new RuntimeException("agent_id must not be null");
            }

            WeComManager weComManager = WeComContextHolder.getWeChatManager();
            String accessToken = weComManager.tokenService().getAccessToken(app);
            template.query(WeComClient.ACCESS_TOKEN, accessToken);
        }
    }
}
