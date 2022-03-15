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
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.AddPermissionRequest;
import software.amazon.awssdk.services.lambda.model.AddPermissionResponse;
import software.amazon.awssdk.services.lambda.model.RemovePermissionRequest;
import software.amazon.awssdk.services.lambda.model.RemovePermissionResponse;

@Slf4j
@Component
public class AwsOperationImpl implements AwsOperation {

    private static final String DEFAULT_INPUT = "{}";
    private static final String EXPRESSION_TEMPLATE = "rate(%s minutes)";
    private static final String DEFAULT_EVENT_BUS = "default";
    private static final String RULE_NAME_FORMAT = "%s-%s";
    private static final String STATEMENT_ID_FORMAT = "AWSEvents_%s_%s";
    public static final String ACTION = "lambda:InvokeFunction";
    public static final String PRINCIPAL = "events.amazonaws.com";

    private final ObjectMapper objectMapper;
    private final AwsProperties awsProperties;
    private final AesEncryptor aesEncryptor;
    private final EventBridgeClient eventBridgeClient;
    private final LambdaClient lambdaClient;

    public AwsOperationImpl(ObjectMapper objectMapper, AwsProperties awsProperties,
        AesEncryptor aesEncryptor, EventBridgeClient eventBridgeClient,
        LambdaClient lambdaClient) {
        this.objectMapper = objectMapper;
        this.awsProperties = awsProperties;
        this.aesEncryptor = aesEncryptor;
        this.eventBridgeClient = eventBridgeClient;
        this.lambdaClient = lambdaClient;
    }

    private String generateRuleName(String taskName, String alarmLevel) {
        return String.format(RULE_NAME_FORMAT, taskName, alarmLevel);
    }

    @Override
    public String createRuleAndReturnArn(TaskEntity task, TaskAlertRule taskAlertRule) {
        Integer minuteInterval = getMinuteInterval(taskAlertRule);
        PutRuleRequest putRuleRequest = PutRuleRequest.builder()
            .name(generateRuleName(task.getName(), taskAlertRule.getAlarmLevel()))
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
            .rule(generateRuleName(task.getName(), taskAlertRule.getAlarmLevel()))
            .eventBusName(DEFAULT_EVENT_BUS)
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
    public void addPermissionToInvokeLambdaFunction(
        String ruleArn, String ruleTargetId, String taskName, String alarmLevel) {
        AddPermissionRequest addPermissionRequest = AddPermissionRequest.builder()
            .action(ACTION)
            .functionName(awsProperties.getLambdaArn())
            .principal(PRINCIPAL)
            .sourceArn(ruleArn)
            .statementId(String.format(STATEMENT_ID_FORMAT, generateRuleName(taskName, alarmLevel), ruleTargetId))
            .build();
        AddPermissionResponse response = lambdaClient.addPermission(addPermissionRequest);
        log.info("Lambda-addPermission: {}", response);
    }

    @Override
    public void removePermissionForInvokeFunction(String taskName, String alarmLevel, String ruleTargetId) {
        RemovePermissionRequest request = RemovePermissionRequest.builder()
            .functionName(awsProperties.getLambdaArn())
            .statementId(String.format(STATEMENT_ID_FORMAT, generateRuleName(taskName, alarmLevel), ruleTargetId))
            .build();
        RemovePermissionResponse response = lambdaClient.removePermission(request);
        log.info("Lambda-removePermission: {}", response);
    }

    @Override
    public void deleteRule(TaskEntity task, TaskAlertRule taskAlertRule) {
        DeleteRuleResponse response = eventBridgeClient.deleteRule(
            DeleteRuleRequest.builder().name(generateRuleName(task.getName(), taskAlertRule.getAlarmLevel())).build());
        log.info("DeleteRuleResponse: {}", response);
    }

    @Override
    public void removeTarget(String taskName, String alarmLevel, List<String> targets) {
        RemoveTargetsRequest request = RemoveTargetsRequest.builder()
            .rule(generateRuleName(taskName, alarmLevel)).ids(targets).build();
        RemoveTargetsResponse response = eventBridgeClient.removeTargets(request);
        log.info("RemoveTargetsResponse: {}", response);
    }

    private String generateInput(TaskEntity task, TaskAlertRule taskAlertRule, String pluginUrl,
        String decryptedConfig) {
        Integer minuteInterval = getMinuteInterval(taskAlertRule);
        AwsLambdaInput input = AwsLambdaInput.builder()
            .id(taskAlertRule.getRuleId().toString())
            .name(generateRuleName(task.getName(), taskAlertRule.getAlarmLevel()))
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