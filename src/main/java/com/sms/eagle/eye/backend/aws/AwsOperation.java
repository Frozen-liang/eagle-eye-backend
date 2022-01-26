package com.sms.eagle.eye.backend.aws;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import java.util.List;

public interface AwsOperation {

    String createRuleAndReturnArn(TaskEntity task);

    String createOrUpdateRuleTarget(TaskEntity task, PluginEntity plugin, String decryptedConfig);

    void deleteRule(String ruleName);

    void removeTarget(String ruleName, List<String> targets);

}
