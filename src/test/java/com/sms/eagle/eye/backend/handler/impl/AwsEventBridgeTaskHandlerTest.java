package com.sms.eagle.eye.backend.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

// TODO 特殊情形完善（如个别aws操作失败）
public class AwsEventBridgeTaskHandlerTest {

    private final AwsOperation awsOperation = mock(AwsOperation.class);
    private final ThirdPartyMappingService thirdPartyMappingService = mock(ThirdPartyMappingService.class);

    private final AwsEventBridgeTaskHandler taskHandler = spy(
        new AwsEventBridgeTaskHandler(awsOperation, thirdPartyMappingService));

    /**
     * {@link AwsEventBridgeTaskHandler#startTask(TaskOperationRequest)}
     *
     * <p>情形1：
     */
    @Test
    void startTask_test1() {
        // mock TaskOperationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock operationRequest.getPlugin()
        PluginEntity pluginEntity = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(pluginEntity);
        // mock operationRequest.getPlugin()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singleton(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock awsOperation.createRuleAndReturnArn()
        String ruleArn = "ruleArn";
        when(awsOperation.createRuleAndReturnArn(taskEntity, taskAlertRule)).thenReturn(ruleArn);
        // mock AwsEventBridgeTaskHandler.generalOperation()
        doNothing().when(taskHandler)
            .generalOperation(ruleArn, taskEntity, pluginEntity, decryptedConfig, taskAlertRule);
        // invoke
        taskHandler.startTask(operationRequest);
        // verify
        verify(taskHandler).generalOperation(ruleArn, taskEntity, pluginEntity, decryptedConfig, taskAlertRule);
    }

    /**
     * {@link AwsEventBridgeTaskHandler#stopTask(TaskOperationRequest)}
     *
     * <p>根据每一条告警规则中的 {@link TaskAlertRule#getRuleId()}
     * 找到对应的 Aws EventBridge 的 ruleArn，
     * 先将 rule 下绑定的所有 target 删除，
     * 再将 rule 删除.
     *
     * <p>情形1：
     */
    @Test
    void stopTask_test1() {
        // mock TaskOperationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock operationRequest.getPlugin()
        PluginEntity pluginEntity = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(pluginEntity);
        // mock operationRequest.getPlugin()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getRuleId()
        Long ruleId = 1L;
        when(taskAlertRule.getRuleId()).thenReturn(ruleId);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singleton(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock thirdPartyMappingService.getAwsRuleArnByTaskAlertRuleId()
        String ruleArn = "ruleArn";
        doReturn(Optional.of(ruleArn)).when(thirdPartyMappingService).getAwsRuleArnByTaskAlertRuleId(ruleId);
        // mock taskHandler.removeRuleTargetIfPresent()
        doNothing().when(taskHandler).removeRuleTargetIfPresent(taskEntity, taskAlertRule);
        // mock awsOperation.deleteRule()
        doNothing().when(awsOperation).deleteRule(taskEntity, taskAlertRule);
        // invoke
        taskHandler.stopTask(operationRequest);
        // verify
        verify(taskHandler).removeRuleTargetIfPresent(taskEntity, taskAlertRule);
        verify(awsOperation).deleteRule(taskEntity, taskAlertRule);
    }

    /**
     * {@link AwsEventBridgeTaskHandler#updateTask(TaskOperationRequest)}
     *
     * <p>根据每一条告警规则中的 {@link TaskAlertRule#getRuleId()}
     * 找到对应的 Aws EventBridge 的 ruleArn，
     * 先将 rule 下绑定的所有 target 删除，
     * 再绑定新的 target.
     *
     * <p>情形1：
     */
    @Test
    void updateTask_test1() {
        // mock TaskOperationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock operationRequest.getPlugin()
        PluginEntity pluginEntity = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(pluginEntity);
        // mock operationRequest.getPlugin()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getRuleId()
        Long ruleId = 1L;
        when(taskAlertRule.getRuleId()).thenReturn(ruleId);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singleton(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock thirdPartyMappingService.getAwsRuleArnByTaskAlertRuleId()
        String ruleArn = "ruleArn";
        doReturn(Optional.of(ruleArn)).when(thirdPartyMappingService).getAwsRuleArnByTaskAlertRuleId(ruleId);
        // mock AwsEventBridgeTaskHandler.generalOperation()
        doNothing().when(taskHandler)
            .generalOperation(ruleArn, taskEntity, pluginEntity, decryptedConfig, taskAlertRule);
        // invoke
        taskHandler.updateTask(operationRequest);
        // verify
        verify(taskHandler).generalOperation(ruleArn, taskEntity, pluginEntity, decryptedConfig, taskAlertRule);
    }

    /**
     * {@link AwsEventBridgeTaskHandler#generalOperation(String, TaskEntity, PluginEntity, String, TaskAlertRule)}
     *
     * <p>删除 rule 下绑定的所有 target，
     * 绑定一条新的 target，并为该 target 添加 lambda 的执行权限，
     * 再保存 {@link TaskAlertRule#getRuleId()} 和 ruleArn 以及 ruleTarget 的对应关系.
     *
     * <p>情形1：
     */
    @Test
    void generalOperation_test1() {
        String ruleArn = "ruleArn";
        String decryptedConfig = "decryptedConfig";
        // mock TaskEntity
        TaskEntity taskEntity = mock(TaskEntity.class);
        // mock taskEntity.getName()
        String taskName = "taskName";
        when(taskEntity.getName()).thenReturn(taskName);
        // mock PluginEntity
        PluginEntity pluginEntity = mock(PluginEntity.class);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskAlertRule.getRuleId()
        Long ruleId = 1L;
        when(taskAlertRule.getRuleId()).thenReturn(ruleId);
        // mock AwsEventBridgeTaskHandler.removeRuleTargetIfPresent()
        doNothing().when(taskHandler).removeRuleTargetIfPresent(taskEntity, taskAlertRule);
        // mock awsOperation.createOrUpdateRuleTarget
        String targetId = "targetId";
        doReturn(targetId).when(awsOperation)
            .createOrUpdateRuleTarget(taskEntity, taskAlertRule, pluginEntity, decryptedConfig);
        // mock awsOperation.addPermissionToInvokeLambdaFunction
        doNothing().when(awsOperation).addPermissionToInvokeLambdaFunction(ruleArn, targetId, taskName, alarmLevel);
        // mock thirdPartyMappingService.addAwsRuleMapping
        doNothing().when(thirdPartyMappingService).addAwsRuleMapping(ruleId, ruleArn);
        doNothing().when(thirdPartyMappingService).addAwsRuleTargetMapping(ruleId, targetId);
        // invoke
        taskHandler.generalOperation(ruleArn, taskEntity, pluginEntity, decryptedConfig, taskAlertRule);
        // verify
        verify(awsOperation).createOrUpdateRuleTarget(taskEntity, taskAlertRule, pluginEntity, decryptedConfig);
        verify(awsOperation).addPermissionToInvokeLambdaFunction(ruleArn, targetId, taskName, alarmLevel);
        verify(thirdPartyMappingService).addAwsRuleMapping(ruleId, ruleArn);
        verify(thirdPartyMappingService).addAwsRuleTargetMapping(ruleId, targetId);
    }

    /**
     * {@link AwsEventBridgeTaskHandler#removeRuleTargetIfPresent(TaskEntity, TaskAlertRule)}
     *
     * <p>根据 {@link TaskAlertRule#getRuleId()} 查询对应的 ruleTarget 列表，
     * 调用 aws接口 删除所有的 ruleTarget，同时删除 ruleTarget 对 lambda 的执行权限，
     * 最后删除 {@link TaskAlertRule#getRuleId()} 和 ruleArn 以及 ruleTarget 的对应关系.
     *
     * <p>情形1：
     */
    @Test
    void removeRuleTargetIfPresent_test_1() {
        // mock TaskEntity
        TaskEntity taskEntity = mock(TaskEntity.class);
        // mock taskEntity.getName()
        String taskName = "taskName";
        when(taskEntity.getName()).thenReturn(taskName);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskAlertRule.getRuleId()
        Long ruleId = 1L;
        when(taskAlertRule.getRuleId()).thenReturn(ruleId);
        // mock thirdPartyMappingService.getAwsRuleTargetList()
        String ruleTarget = "ruleTarget";
        List<String> ruleTargetList = Arrays.asList(ruleTarget, ruleTarget);
        when(thirdPartyMappingService.getAwsRuleTargetList(ruleId))
            .thenReturn(ruleTargetList);
        doNothing().when(awsOperation).removeTarget(taskName, alarmLevel, ruleTargetList);
        doNothing().when(awsOperation).removePermissionForInvokeFunction(taskName, alarmLevel, ruleTarget);
        doNothing().when(thirdPartyMappingService).removeAwsRuleTargetMapping(ruleId);
        doNothing().when(thirdPartyMappingService).removeAwsRuleMapping(ruleId);
        // invoke
        taskHandler.removeRuleTargetIfPresent(taskEntity, taskAlertRule);
        // verify
        verify(awsOperation).removeTarget(taskName, alarmLevel, ruleTargetList);
        verify(awsOperation, times(2))
            .removePermissionForInvokeFunction(taskName, alarmLevel, ruleTarget);
        verify(thirdPartyMappingService).removeAwsRuleTargetMapping(ruleId);
        verify(thirdPartyMappingService).removeAwsRuleMapping(ruleId);
    }

    /**
     * {@link GrpcTaskHandler#ifScheduleBySelf()}
     */
    @Test
    public void ifScheduleBySelf_test() {
        assertThat(taskHandler.ifScheduleBySelf()).isFalse();
    }
}