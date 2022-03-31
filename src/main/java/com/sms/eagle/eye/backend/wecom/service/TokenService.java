package com.sms.eagle.eye.backend.wecom.service;

/**
 * 描述 访问令牌管理.
 **/
public interface TokenService {

    /**
     * 获取企业微信应用的access token.
     */
    String getAccessToken(String applicationName);

    /**
     * 清除应用AccessToken缓存.
     */
    void clearAccessToken(String applicationName);
}
