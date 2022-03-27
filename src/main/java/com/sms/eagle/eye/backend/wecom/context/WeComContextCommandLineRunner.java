package com.sms.eagle.eye.backend.wecom.context;

import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WeComContextCommandLineRunner implements CommandLineRunner {

    private final WeComManager weComManager;

    public WeComContextCommandLineRunner(WeComManager weComManager) {
        this.weComManager = weComManager;
    }

    @Override
    public void run(String... args) {
        WeComContextHolder.setWeChatManager(weComManager);
    }
}