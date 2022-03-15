package com.sms.eagle.eye.backend.handler.impl;

import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AwsEventBridgeTaskHandler implements TaskHandler {

    private final AwsOperation awsOperation;
    private final ThirdPartyMappingService thirdPartyMappingService;

    public AwsEventBridgeTaskHandler(
        AwsOperation awsOperation, ThirdPartyMappingService thirdPartyMappingService) {
        this.awsOperation = awsOperation;
        this.thirdPartyMappingService = thirdPartyMappingService;
    }

    /**
     * 每一条任务 可能含 多条告警规则（对应不同告警规则）
     * 需要为 每一条告警规则，在 EventBridge 中创建 rule.
     */
    @Override
    public void startTask(TaskOperationRequest request) {
        request.getAlertRules().forEach(taskAlertRule -> {
            String ruleArn = awsOperation.createRuleAndReturnArn(request.getTask(), taskAlertRule);
            generalOperation(ruleArn, request.getTask(),
                request.getPlugin(), request.getDecryptedConfig(), taskAlertRule);
        });
    }

    @Override
    public void stopTask(TaskOperationRequest request) {
        request.getAlertRules().forEach(taskAlertRule -> {
            Optional<String> awsRuleOptional = thirdPartyMappingService
                .getAwsRuleArnByTaskAlertRuleId(taskAlertRule.getRuleId());
            awsRuleOptional.ifPresentOrElse(ruleArn -> {
                removeRuleTargetIfPresent(request.getTask(), taskAlertRule);
                awsOperation.deleteRule(request.getTask(), taskAlertRule);
                log.info("delete event bridge, {}", ruleArn);
            }, () -> log.info("delete error"));
        });
    }

    @Override
    public void updateTask(TaskOperationRequest request) {
        request.getAlertRules().forEach(taskAlertRule -> {
            Optional<String> awsRuleOptional =
                thirdPartyMappingService.getAwsRuleArnByTaskAlertRuleId(taskAlertRule.getRuleId());
            awsRuleOptional.ifPresent(ruleArn -> generalOperation(
                ruleArn, request.getTask(), request.getPlugin(), request.getDecryptedConfig(), taskAlertRule));
        });
    }

    @Override
    public Boolean ifScheduleBySelf() {
        return Boolean.FALSE;
    }

    /**
     * 记录与 ruleArn 与 TaskAlertRule 的对应关系，并将 lambda 绑定至 ruleTarget.
     */
    private void generalOperation(String ruleArn, TaskEntity task,
        PluginEntity plugin, String decryptedConfig, TaskAlertRule taskAlertRule) {
        removeRuleTargetIfPresent(task, taskAlertRule);
        String targetId = awsOperation.createOrUpdateRuleTarget(task, taskAlertRule, plugin, decryptedConfig);
        awsOperation.addPermissionToInvokeLambdaFunction(
            ruleArn, targetId, task.getName(), taskAlertRule.getAlarmLevel());
        thirdPartyMappingService.addAwsRuleMapping(taskAlertRule.getRuleId(), ruleArn);
        thirdPartyMappingService.addAwsRuleTargetMapping(taskAlertRule.getRuleId(), targetId);
    }

    private void removeRuleTargetIfPresent(TaskEntity task, TaskAlertRule taskAlertRule) {
        List<String> ruleTargetList = thirdPartyMappingService.getAwsRuleTargetList(taskAlertRule.getRuleId());
        awsOperation.removeTarget(task.getName(), taskAlertRule.getAlarmLevel(), ruleTargetList);
        if (CollectionUtils.isNotEmpty(ruleTargetList)) {
            for (String ruleTarget: ruleTargetList) {
                awsOperation.removePermissionForInvokeFunction(
                    task.getName(), taskAlertRule.getAlarmLevel(), ruleTarget);
            }
            thirdPartyMappingService.removeAwsRuleTargetMapping(taskAlertRule.getRuleId());
        }
    }
}