package com.sms.eagle.eye.backend.handler.impl;

import com.sms.eagle.eye.backend.aws.AwsOperation;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
        Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(request.getTask().getId());
        awsRuleOptional.ifPresentOrElse(ruleArn -> {
            log.info("awsOperation, update and enable ");
        }, () -> {
            String ruleArn = awsOperation.createRuleAndReturnArn(request.getTask());
            awsOperation.createRuleTargetAndReturnId(
                request.getTask(), request.getPlugin(), request.getDecryptedConfig());
            thirdPartyMappingService.addAwsRuleMapping(request.getTask().getId(), ruleArn);
        });

    }

    @Override
    public void stopTask(TaskOperationRequest request) {
        Optional<String> awsRuleOptional = thirdPartyMappingService.getAwsRuleArnByTaskId(request.getTask().getId());
        awsRuleOptional.ifPresentOrElse(ruleArn -> {
            log.info("delete event bridge, {}", ruleArn);
        }, () -> log.info("delete error"));
    }

    @Override
    public void updateTask(TaskOperationRequest request) {

    }

    @Override
    public Boolean ifScheduleBySelf() {
        return Boolean.FALSE;
    }
}