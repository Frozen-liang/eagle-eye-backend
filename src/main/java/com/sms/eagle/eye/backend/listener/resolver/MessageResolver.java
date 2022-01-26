package com.sms.eagle.eye.backend.listener.resolver;

public interface MessageResolver {

    void resolve(String payload, String group);
}