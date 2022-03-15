package com.sms.eagle.eye.backend.aws;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import java.util.List;

public interface AwsOperation {

    String createRuleAndReturnArn(TaskEntity task, TaskAlertRule taskAlertRule);

    String createOrUpdateRuleTarget(TaskEntity task, TaskAlertRule taskAlertRule, PluginEntity plugin,
        String decryptedConfig);

    void addPermissionToInvokeLambdaFunction(String ruleArn, String ruleTargetId, String taskName, String alarmLevel);

    void removePermissionForInvokeFunction(String taskName, String alarmLevel, String ruleTargetId);

    void deleteRule(TaskEntity task, TaskAlertRule taskAlertRule);

    void removeTarget(String taskName, String alarmLevel, List<String> targets);

}
