package com.sms.eagle.eye.backend.aws;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;

public interface AwsOperation {

    String createRuleAndReturnArn(TaskEntity task);

    String createRuleTargetAndReturnId(TaskEntity task, PluginEntity plugin, String decryptedConfig);
}
