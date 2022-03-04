package com.sms.eagle.eye.backend.wecom.context;

import com.sms.eagle.eye.backend.wecom.manager.WeComManager;

/**
 * 描述 企业微信上下文工具类.
 **/
public class WeComContextHolder {

    private static WeComManager WE_CHAT_MANAGER;

    public static void setWeChatManager(WeComManager weComManager) {
        WE_CHAT_MANAGER = weComManager;
    }

    public static WeComManager getWeChatManager() {
        return WE_CHAT_MANAGER;
    }
}
