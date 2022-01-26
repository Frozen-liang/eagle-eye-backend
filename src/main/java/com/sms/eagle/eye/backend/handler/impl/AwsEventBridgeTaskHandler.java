package com.sms.eagle.eye.backend.handler.impl;

import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.handler.TaskHandler;
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

    @Override
    public void startTask(TaskOperationRequest request) {
        String ruleArn = awsOperation.createRuleAndReturnArn(request.getTask());
        generalOperation(ruleArn, request);
    }

    @Override
    public void stopTask(TaskOperationRequest request) {
        Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(request.getTask().getId());
        awsRuleOptional.ifPresentOrElse(ruleArn -> {
            removeRuleTargetIfPresent(request.getTask());
            awsOperation.deleteRule(request.getTask().getName());
            log.info("delete event bridge, {}", ruleArn);
        }, () -> log.info("delete error"));
    }

    @Override
    public void updateTask(TaskOperationRequest request) {
        Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(request.getTask().getId());
        awsRuleOptional.ifPresent(ruleArn -> generalOperation(ruleArn, request));
    }

    @Override
    public Boolean ifScheduleBySelf() {
        return Boolean.FALSE;
    }

    private void generalOperation(String ruleArn, TaskOperationRequest request) {
        removeRuleTargetIfPresent(request.getTask());
        String targetId = awsOperation.createOrUpdateRuleTarget(
            request.getTask(), request.getPlugin(), request.getDecryptedConfig());
        thirdPartyMappingService.addAwsRuleMapping(request.getTask().getId(), ruleArn);
        thirdPartyMappingService.addAwsRuleTargetMapping(request.getTask().getId(), targetId);
    }

    private void removeRuleTargetIfPresent(TaskEntity task) {
        List<String> ruleTargetList = thirdPartyMappingService.getAwsRuleTargetList(task.getId());
        if (CollectionUtils.isNotEmpty(ruleTargetList)) {
            awsOperation.removeTarget(task.getName(), ruleTargetList);
            thirdPartyMappingService.removeAwsRuleTargetMapping(task.getId());
        }
    }
}