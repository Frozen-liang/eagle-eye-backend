package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.service.AwsEventBridgeService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsResponse;
import software.amazon.awssdk.services.eventbridge.model.Target;

@Service
public class AwsEventBridgeServiceImpl implements AwsEventBridgeService {

    private final EventBridgeClient eventBridgeClient;

    public AwsEventBridgeServiceImpl(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    @Override
    public boolean pushRuleAndTarget(String ruleName) {
        // PutRule
        PutRuleRequest putRuleRequest = PutRuleRequest.builder()
            .name(ruleName)
            .scheduleExpression("rate(1 minutes)")
            .description("description")
            .build();
        PutRuleResponse putRuleResponse = eventBridgeClient.putRule(putRuleRequest);
        // PutTarget
        PutTargetsRequest putTargetsRequest = PutTargetsRequest.builder()
            .rule(ruleName)
            .targets(Target.builder()
                .id("") //指定规则中目标的 ID
                .arn("") //The Amazon Resource Name (ARN) of the target. -> function arn
                .roleArn("")
                .input("")
                .build())
            .build();
        PutTargetsResponse putTargetsResponse = eventBridgeClient.putTargets(putTargetsRequest);

        return false;
    }
}