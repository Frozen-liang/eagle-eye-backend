package com.sms.eagle.eye.backend.aws;

import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import java.util.List;

public interface AwsOperation {

    String createRuleAndReturnArn(TaskEntity task);

    String createRuleTargetAndReturnId(TaskEntity task, PluginEntity plugin, List<PluginConfigFieldEntity> fields);
}
