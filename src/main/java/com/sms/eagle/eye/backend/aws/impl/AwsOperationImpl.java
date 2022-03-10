package com.sms.eagle.eye.backend.aws.impl;

import static com.sms.eagle.eye.backend.utils.TaskScheduleUtil.getMinuteInterval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.aws.dto.AwsAlertRuleDto;
import com.sms.eagle.eye.backend.aws.dto.AwsLambdaInput;
import com.sms.eagle.eye.backend.common.encrypt.AesEncryptor;
import com.sms.eagle.eye.backend.config.AwsProperties;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import io.vavr.control.Try;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.DeleteRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.DeleteRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsResponse;
import software.amazon.awssdk.services.eventbridge.model.RemoveTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.RemoveTargetsResponse;
import software.amazon.awssdk.services.eventbridge.model.Target;

@Slf4j
@Component
public class AwsOperationImpl implements AwsOperation {

    private static final String DEFAULT_INPUT = "{}";
    private static final String EXPRESSION_TEMPLATE = "rate(%s minutes)";
    private static final String RULE_NAME_FORMAT = "%s-%s";

    private final ObjectMapper objectMapper;
    private final AwsProperties awsProperties;
    private final AesEncryptor aesEncryptor;
    private final EventBridgeClient eventBridgeClient;

    public AwsOperationImpl(ObjectMapper objectMapper, AwsProperties awsProperties,
        AesEncryptor aesEncryptor, EventBridgeClient eventBridgeClient) {
        this.objectMapper = objectMapper;
        this.awsProperties = awsProperties;
        this.aesEncryptor = aesEncryptor;
        this.eventBridgeClient = eventBridgeClient;
    }

    private String generateRuleName(TaskEntity task, TaskAlertRule taskAlertRule) {
        return String.format(task.getName(), taskAlertRule.getAlarmLevel());
    }

    @Override
    public String createRuleAndReturnArn(TaskEntity task, TaskAlertRule taskAlertRule) {
        Integer minuteInterval = getMinuteInterval(taskAlertRule);
        PutRuleRequest putRuleRequest = PutRuleRequest.builder()
            .name(generateRuleName(task, taskAlertRule))
            .scheduleExpression(String.format(EXPRESSION_TEMPLATE, minuteInterval))
            .description(task.getDescription())
            .build();
        PutRuleResponse putRuleResponse = eventBridgeClient.putRule(putRuleRequest);
        log.info("PutRuleResponse: {}", putRuleResponse);
        return putRuleResponse.ruleArn();
    }

    @Override
    public String createOrUpdateRuleTarget(TaskEntity task, TaskAlertRule taskAlertRule, PluginEntity plugin,
        String decryptedConfig) {
        String id = UUID.randomUUID().toString();
        PutTargetsRequest putTargetsRequest = PutTargetsRequest.builder()
            .rule(generateRuleName(task, taskAlertRule))
            .targets(Target.builder()
                .id(id)
                .arn(awsProperties.getLambdaArn())
                .input(generateInput(task, taskAlertRule, plugin.getUrl(), decryptedConfig))
                .build())
            .build();
        PutTargetsResponse response = eventBridgeClient.putTargets(putTargetsRequest);
        log.info("PutTargetsResponse: {}", response);
        return id;
    }

    @Override
    public void deleteRule(TaskEntity task, TaskAlertRule taskAlertRule) {
        DeleteRuleResponse response = eventBridgeClient.deleteRule(
            DeleteRuleRequest.builder().name(generateRuleName(task, taskAlertRule)).build());
        log.info("DeleteRuleResponse: {}", response);
    }

    @Override
    public void removeTarget(String ruleName, List<String> targets) {
        RemoveTargetsRequest request = RemoveTargetsRequest.builder()
            .rule(ruleName).ids(targets).build();
        RemoveTargetsResponse response = eventBridgeClient.removeTargets(request);
        log.info("RemoveTargetsResponse: {}", response);
    }

    private String generateInput(TaskEntity task, TaskAlertRule taskAlertRule, String pluginUrl,
        String decryptedConfig) {
        Integer minuteInterval = getMinuteInterval(taskAlertRule);
        AwsLambdaInput input = AwsLambdaInput.builder()
            .id(taskAlertRule.getRuleId().toString())
            .name(generateRuleName(task, taskAlertRule))
            .description(task.getDescription())
            .alertRule(Collections.singletonList(AwsAlertRuleDto.builder()
                .interval(minuteInterval)
                .rule(taskAlertRule.getDecryptedAlertRule())
                .build()))
            .pluginUrl(pluginUrl)
            .queueUrl(awsProperties.getQueueUrl())
            .decryptKey(aesEncryptor.getSecretKey())
            .payload(aesEncryptor.encrypt(decryptedConfig))
            .build();
        return Try.of(() -> objectMapper.writeValueAsString(input)).getOrElse(DEFAULT_INPUT);
    }
}