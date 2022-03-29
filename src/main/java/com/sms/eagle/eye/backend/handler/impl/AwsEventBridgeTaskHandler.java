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
     * 每一条任务可能含多条告警规则（对应不同告警规则）
     * 为每一条 taskAlertRule 创建 EventBridge rule，并绑定 target 关系.
     */
    @Override
    public void startTask(TaskOperationRequest request) {
        request.getAlertRules().forEach(taskAlertRule -> {
            String ruleArn = awsOperation.createRuleAndReturnArn(request.getTask(), taskAlertRule);
            generalOperation(ruleArn, request.getTask(),
                request.getPlugin(), request.getDecryptedConfig(), taskAlertRule);
        });
    }

    /**
     * 根据每一条告警规则中的 {@link TaskAlertRule#getRuleId()}
     * 找到对应的 Aws EventBridge 的 ruleArn，
     * 先将 rule 下绑定的所有 target 删除，
     * 再将 rule 删除.
     */
    @Override
    public void stopTask(TaskOperationRequest request) {
        request.getAlertRules().forEach(taskAlertRule -> {
            Optional<String> awsRuleOptional = thirdPartyMappingService
                .getAwsRuleArnByTaskAlertRuleId(taskAlertRule.getRuleId());
            awsRuleOptional.ifPresentOrElse(ruleArn -> {
                removeRuleTargetIfPresent(request.getTask(), taskAlertRule);
                awsOperation.deleteRule(request.getTask(), taskAlertRule);
                log.info("delete event bridge, {}", ruleArn);
            }, () -> log.info("no ruleArn to remove"));
        });
    }

    /**
     * 根据每一条告警规则中的 {@link TaskAlertRule#getRuleId()}
     * 找到对应的 Aws EventBridge 的 ruleArn，
     * 先将 rule 下绑定的所有 target 删除，
     * 再绑定新的 target.
     */
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
     * 删除 rule 下绑定的所有 target，
     * 绑定一条新的 target，并为该 target 添加 lambda 的执行权限，
     * 再保存 {@link TaskAlertRule#getRuleId()} 和 ruleArn 以及 ruleTarget 的对应关系.
     */
    protected void generalOperation(String ruleArn, TaskEntity task,
        PluginEntity plugin, String decryptedConfig, TaskAlertRule taskAlertRule) {
        removeRuleTargetIfPresent(task, taskAlertRule);
        String targetId = awsOperation.createOrUpdateRuleTarget(task, taskAlertRule, plugin, decryptedConfig);
        awsOperation.addPermissionToInvokeLambdaFunction(
            ruleArn, targetId, task.getName(), taskAlertRule.getAlarmLevel());
        thirdPartyMappingService.addAwsRuleMapping(taskAlertRule.getRuleId(), ruleArn);
        thirdPartyMappingService.addAwsRuleTargetMapping(taskAlertRule.getRuleId(), targetId);
    }

    /**
     * 根据 {@link TaskAlertRule#getRuleId()} 查询对应的 ruleTarget 列表，
     * 调用 aws接口 删除所有的 ruleTarget，同时删除 ruleTarget 对 lambda 的执行权限，
     * 最后删除 {@link TaskAlertRule#getRuleId()} 和 ruleArn 以及 ruleTarget 的对应关系.
     */
    protected void removeRuleTargetIfPresent(TaskEntity task, TaskAlertRule taskAlertRule) {
        List<String> ruleTargetList = thirdPartyMappingService.getAwsRuleTargetList(taskAlertRule.getRuleId());
        if (CollectionUtils.isNotEmpty(ruleTargetList)) {
            awsOperation.removeTarget(task.getName(), taskAlertRule.getAlarmLevel(), ruleTargetList);
            for (String ruleTarget : ruleTargetList) {
                awsOperation.removePermissionForInvokeFunction(
                    task.getName(), taskAlertRule.getAlarmLevel(), ruleTarget);
            }
            thirdPartyMappingService.removeAwsRuleTargetMapping(taskAlertRule.getRuleId());
        }
        thirdPartyMappingService.removeAwsRuleMapping(taskAlertRule.getRuleId());
    }
}