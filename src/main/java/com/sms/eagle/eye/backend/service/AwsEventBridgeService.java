package com.sms.eagle.eye.backend.service;

public interface AwsEventBridgeService {

    boolean pushRuleAndTarget(String ruleName);

}
