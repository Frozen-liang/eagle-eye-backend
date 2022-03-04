package com.sms.eagle.eye.backend.wecom.client;

import com.sms.eagle.eye.backend.wecom.config.WeComFeignConfiguration;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wecom", url = "https://qyapi.weixin.qq.com", path = "cgi-bin",
    configuration = WeComFeignConfiguration.class)
public interface WeComClient {

    String HEAD_KEY = "app";
    String HEAD = HEAD_KEY + "={app}";
    String ACCESS_TOKEN = "access_token";
    String GET_TOKEN = "/gettoken";

    /**
     * 获取应用 access token.
     *
     * @param corpId 企业号
     * @param secret 密匙
     */
    @GetMapping("gettoken")
    AccessTokenResponse getAccessToken(@RequestParam("corpid") String corpId,
        @RequestParam("corpsecret") String secret);

    /**
     * 发送消息.
     *
     * @param request 请求体
     * @param app     应用名
     * @return SendMessageResponse
     */
    @PostMapping(value = "message/send", headers = HEAD)
    SendMessageResponse sendMessage(SendMessageRequest request, @RequestParam(HEAD_KEY) String app);

}
