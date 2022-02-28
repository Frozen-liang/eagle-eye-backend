package com.sms.eagle.eye.backend.aws;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import java.util.List;

public interface AwsOperation {

    String createRuleAndReturnArn(TaskEntity task, TaskAlertRule taskAlertRule);

    String createOrUpdateRuleTarget(TaskEntity task, PluginEntity plugin, String decryptedConfig);

    void deleteRule(String ruleName);

    void removeTarget(String ruleName, List<String> targets);

}
