package com.sms.eagle.eye.backend.aws.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.common.encrypt.AesEncryptor;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.config.AwsProperties;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.AwsLambdaInput;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import io.vavr.control.Try;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
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
    private final PluginConfigResolver pluginConfigResolver;
    private final EventBridgeClient eventBridgeClient;

    public AwsOperationImpl(ObjectMapper objectMapper, AwsProperties awsProperties,
        AesEncryptor aesEncryptor, PluginConfigResolver pluginConfigResolver,
        EventBridgeClient eventBridgeClient) {
        this.objectMapper = objectMapper;
        this.awsProperties = awsProperties;
        this.aesEncryptor = aesEncryptor;
        this.pluginConfigResolver = pluginConfigResolver;
        this.eventBridgeClient = eventBridgeClient;
    }

    @Override
    public String createRuleAndReturnArn(TaskEntity task) {
        Integer minuteInterval = convertToMinuteInterval(task);
        PutRuleRequest putRuleRequest = PutRuleRequest.builder()
            .name(task.getName())
            .scheduleExpression(String.format(EXPRESSION_TEMPLATE, minuteInterval))
            .description(task.getDescription())
            .build();
        PutRuleResponse putRuleResponse = eventBridgeClient.putRule(putRuleRequest);
        return putRuleResponse.ruleArn();
    }

    @Override
    public String createRuleTargetAndReturnId(TaskEntity task, PluginEntity plugin,
        List<PluginConfigFieldEntity> fields) {
        String id = UUID.randomUUID().toString();
        PutTargetsRequest putTargetsRequest = PutTargetsRequest.builder()
            .rule(task.getName())
            .targets(Target.builder()
                .id(id)
                .arn(awsProperties.getLambdaArn())
                .roleArn(awsProperties.getRoleArn())
                .input(generateInput(task, plugin.getUrl(), fields))
                .build())
            .build();
        eventBridgeClient.putTargets(putTargetsRequest);
        return id;
    }

    private String generateInput(TaskEntity task, String pluginUrl, List<PluginConfigFieldEntity> fields) {
        Integer minuteInterval = convertToMinuteInterval(task);
        String config = pluginConfigResolver.decryptToString(fields, task.getPluginConfig());
        AwsLambdaInput input = AwsLambdaInput.builder()
            .id(task.getId().toString())
            .name(task.getName())
            .description(task.getDescription())
            .interval(minuteInterval)
            .pluginUrl(pluginUrl)
            .webhookUrl(awsProperties.getWebhookUrl())
            .decryptKey(aesEncryptor.getSecretKey())
            .payload(aesEncryptor.encrypt(config))
            .build();
        return Try.of(() -> objectMapper.writeValueAsString(input)).getOrElse(DEFAULT_INPUT);
    }

    private Integer convertToMinuteInterval(TaskEntity task) {
        return TaskScheduleUnit.resolve(task.getScheduleUnit())
            .getConvertToMinute().apply(task.getScheduleInterval());
    }
}