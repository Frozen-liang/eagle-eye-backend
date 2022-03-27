package com.sms.eagle.eye.backend.context;

import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TokenContextCommandLineRunner implements CommandLineRunner {

    private final NerkoTokenService nerkoTokenService;

    public TokenContextCommandLineRunner(NerkoTokenService nerkoTokenService) {
        this.nerkoTokenService = nerkoTokenService;
    }

    @Override
    public void run(String... args) throws Exception {
        AccessTokenContextHolder.setTokenService(nerkoTokenService);
    }
}