package com.sms.eagle.eye.backend.aws.impl;

import static com.sms.eagle.eye.backend.utils.TaskScheduleUtil.getMinuteInterval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.common.encrypt.AesEncryptor;
import com.sms.eagle.eye.backend.config.AwsProperties;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.AwsLambdaInput;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.DeleteRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.Target;

@Component
public class AwsOperationImpl implements AwsOperation {

    private static final String DEFAULT_INPUT = "{}";
    private static final String EXPRESSION_TEMPLATE = "rate(%s minutes)";

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

    @Override
    public String createRuleAndReturnArn(TaskEntity task) {
        Integer minuteInterval = getMinuteInterval(task);
        PutRuleRequest putRuleRequest = PutRuleRequest.builder()
            .name(task.getName())
            .scheduleExpression(String.format(EXPRESSION_TEMPLATE, minuteInterval))
            .description(task.getDescription())
            .build();
        PutRuleResponse putRuleResponse = eventBridgeClient.putRule(putRuleRequest);
        return putRuleResponse.ruleArn();
    }

    @Override
    public void createOrUpdateRuleTarget(TaskEntity task, PluginEntity plugin,
        String decryptedConfig) {
        PutTargetsRequest putTargetsRequest = PutTargetsRequest.builder()
            .rule(task.getName())
            .targets(Target.builder()
                .arn(awsProperties.getLambdaArn())
                .input(generateInput(task, plugin.getUrl(), decryptedConfig))
                .build())
            .build();
        eventBridgeClient.putTargets(putTargetsRequest);
    }

    @Override
    public void deleteRule(String ruleName) {
        eventBridgeClient.deleteRule(DeleteRuleRequest.builder().name(ruleName).build());
    }

    private String generateInput(TaskEntity task, String pluginUrl, String decryptedConfig) {
        Integer minuteInterval = getMinuteInterval(task);
        AwsLambdaInput input = AwsLambdaInput.builder()
            .id(task.getId().toString())
            .name(task.getName())
            .description(task.getDescription())
            .interval(minuteInterval)
            .pluginUrl(pluginUrl)
            .webhookUrl(awsProperties.getWebhookUrl())
            .updateUrl(awsProperties.getUpdateUrl())
            .decryptKey(aesEncryptor.getSecretKey())
            .payload(aesEncryptor.encrypt(decryptedConfig))
            .build();
        return Try.of(() -> objectMapper.writeValueAsString(input)).getOrElse(DEFAULT_INPUT);
    }
}