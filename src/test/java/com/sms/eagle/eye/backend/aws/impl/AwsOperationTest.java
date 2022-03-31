package com.sms.eagle.eye.backend.aws.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.aws.dto.AwsLambdaInput;
import com.sms.eagle.eye.backend.common.encrypt.AesEncryptor;
import com.sms.eagle.eye.backend.config.AwsProperties;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import com.sms.eagle.eye.backend.utils.TaskScheduleUtil;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.DeleteRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.DeleteRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutRuleRequest;
import software.amazon.awssdk.services.eventbridge.model.PutRuleResponse;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutTargetsResponse;
import software.amazon.awssdk.services.eventbridge.model.RemoveTargetsRequest;
import software.amazon.awssdk.services.eventbridge.model.RemoveTargetsResponse;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.AddPermissionRequest;
import software.amazon.awssdk.services.lambda.model.AddPermissionResponse;
import software.amazon.awssdk.services.lambda.model.RemovePermissionRequest;
import software.amazon.awssdk.services.lambda.model.RemovePermissionResponse;

public class AwsOperationTest {

    private static final String RULE_NAME_FORMAT = "%s-%s";
    private static final MockedStatic<TaskScheduleUtil> TASK_SCHEDULE_UTIL_MOCKED_STATIC
        = mockStatic(TaskScheduleUtil.class);

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final AwsProperties awsProperties = mock(AwsProperties.class);
    private final AesEncryptor aesEncryptor = mock(AesEncryptor.class);
    private final EventBridgeClient eventBridgeClient = mock(EventBridgeClient.class);
    private final LambdaClient lambdaClient = mock(LambdaClient.class);

    private final AwsOperationImpl awsOperation = spy(new AwsOperationImpl(
        objectMapper, awsProperties, aesEncryptor, eventBridgeClient, lambdaClient));

    @AfterAll
    public static void close() {
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.close();
    }


    /**
     * {@link AwsOperationImpl#generateRuleName(String, String)}
     */
    @Test
    void generateRuleName_test_1() {
        String taskName = "taskName";
        String alarmLevel = "alarmLevel";
        // invoke
        String result = awsOperation.generateRuleName(taskName, alarmLevel);
        // assert
        assertThat(result).isEqualTo(String.format(RULE_NAME_FORMAT, taskName, alarmLevel));
    }

    /**
     * {@link AwsOperationImpl#generateInput(TaskEntity, TaskAlertRule, String, String)}
     */
    @Test
    void generateInput_test_1() throws JsonProcessingException {
        // request
        TaskEntity task = mock(TaskEntity.class);
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        String pluginUrl = "pluginUrl";
        String decryptedConfig = "decryptedConfig";
        // mock getMinuteInterval()
        Integer minuteInterval = 1;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(minuteInterval);
        // mock taskAlertRule.getRuleId()
        Long ruleId = 1L;
        when(taskAlertRule.getRuleId()).thenReturn(ruleId);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock task.getName()
        String taskName = "taskName";
        when(task.getName()).thenReturn(taskName);
        // mock awsOperation.generateRuleName()
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // invoke
        String result = awsOperation.generateInput(task, taskAlertRule, pluginUrl, decryptedConfig);
        // verify
        verify(objectMapper).writeValueAsString(any(AwsLambdaInput.class));
    }

    /**
     * {@link AwsOperationImpl#createRuleAndReturnArn(TaskEntity, TaskAlertRule)}
     */
    @Test
    void createRuleAndReturnArn_test() {
        // request
        TaskEntity task = mock(TaskEntity.class);
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock getMinuteInterval()
        Integer minuteInterval = 1;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(minuteInterval);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock task.getName()
        String taskName = "taskName";
        when(task.getName()).thenReturn(taskName);
        // mock awsOperation.generateRuleName()
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock eventBridgeClient.putRule()
        PutRuleResponse putRuleResponse = mock(PutRuleResponse.class);
        when(eventBridgeClient.putRule(any(PutRuleRequest.class))).thenReturn(putRuleResponse);
        // mock putRuleResponse.ruleArn()
        String ruleArn = "ruleArn";
        when(putRuleResponse.ruleArn()).thenReturn(ruleArn);
        // invoke
        String result = awsOperation.createRuleAndReturnArn(task, taskAlertRule);
        // assert
        assertThat(result).isEqualTo(ruleArn);
    }

    /**
     * {@link AwsOperationImpl#createOrUpdateRuleTarget(TaskEntity, TaskAlertRule, PluginEntity, String)}
     */
    @Test
    void createOrUpdateRuleTarget_test() {
        // request
        TaskEntity task = mock(TaskEntity.class);
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        PluginEntity plugin = mock(PluginEntity.class);
        String decryptedConfig = "decryptedConfig";
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock task.getName()
        String taskName = "taskName";
        when(task.getName()).thenReturn(taskName);
        // mock awsOperation.generateRuleName()
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock plugin.getUrl()
        String pluginUrl = "pluginUrl";
        when(plugin.getUrl()).thenReturn(pluginUrl);
        // eventBridgeClient.putTargets
        PutTargetsResponse response = mock(PutTargetsResponse.class);
        when(eventBridgeClient.putTargets(any(PutTargetsRequest.class))).thenReturn(response);
        // invoke
        String id = awsOperation.createOrUpdateRuleTarget(task, taskAlertRule, plugin, decryptedConfig);
        // assert
        assertThat(id).isNotNull();
        verify(eventBridgeClient).putTargets(any(PutTargetsRequest.class));
    }

    /**
     * {@link AwsOperationImpl#addPermissionToInvokeLambdaFunction(String, String, String, String)}
     */
    @Test
    void addPermissionToInvokeLambdaFunction_test() {
        String ruleArn = "ruleArn";
        String ruleTargetId = "ruleTargetId";
        String taskName = "taskName";
        String alarmLevel = "alarmLevel";
        // mock generateRuleName
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock lambdaClient.addPermission()
        AddPermissionResponse response = mock(AddPermissionResponse.class);
        when(lambdaClient.addPermission(any(AddPermissionRequest.class))).thenReturn(response);
        // invoke
        awsOperation.addPermissionToInvokeLambdaFunction(ruleArn, ruleTargetId, taskName, alarmLevel);
        // verify
        verify(lambdaClient).addPermission(any(AddPermissionRequest.class));
    }

    /**
     * {@link AwsOperationImpl#removePermissionForInvokeFunction(String, String, String)}
     */
    @Test
    void removePermissionForInvokeFunction_test() {
        String taskName = "taskName";
        String alarmLevel = "alarmLevel";
        String ruleTargetId = "ruleTargetId";
        // mock generateRuleName
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock lambdaClient.removePermission
        RemovePermissionResponse response = mock(RemovePermissionResponse.class);
        when(lambdaClient.removePermission(any(RemovePermissionRequest.class))).thenReturn(response);
        // invoke
        awsOperation.removePermissionForInvokeFunction(taskName, alarmLevel, ruleTargetId);
        // verify
        verify(lambdaClient).removePermission(any(RemovePermissionRequest.class));
    }

    /**
     * {@link AwsOperationImpl#deleteRule(TaskEntity, TaskAlertRule)}
     */
    @Test
    void deleteRule_test() {
        // request
        TaskEntity task = mock(TaskEntity.class);
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock task.getName()
        String taskName = "taskName";
        when(task.getName()).thenReturn(taskName);
        // mock awsOperation.generateRuleName()
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock eventBridgeClient.deleteRule
        DeleteRuleResponse response = mock(DeleteRuleResponse.class);
        when(eventBridgeClient.deleteRule(any(DeleteRuleRequest.class))).thenReturn(response);
        // invoke
        awsOperation.deleteRule(task, taskAlertRule);
        // verify
        verify(eventBridgeClient).deleteRule(any(DeleteRuleRequest.class));
    }

    @Test
    void removeTarget_test() {
        String taskName = "taskName";
        String alarmLevel = "alarmLevel";
        List<String> targets = List.of("123");
        // mock awsOperation.generateRuleName()
        String ruleName = "ruleName";
        when(awsOperation.generateRuleName(taskName, alarmLevel)).thenReturn(ruleName);
        // mock eventBridgeClient.removeTargets()
        RemoveTargetsResponse response = mock(RemoveTargetsResponse.class);
        when(eventBridgeClient.removeTargets(any(RemoveTargetsRequest.class))).thenReturn(response);
        // invoke
        awsOperation.removeTarget(taskName, alarmLevel, targets);
        // verify
        verify(eventBridgeClient).removeTargets(any(RemoveTargetsRequest.class));
    }
}