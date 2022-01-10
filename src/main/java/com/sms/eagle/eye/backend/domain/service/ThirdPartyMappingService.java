package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import java.util.Optional;

public interface ThirdPartyMappingService extends IService<ThirdPartyMappingEntity> {

    void addPluginSystemUnionIdMapping(LambdaInvokeResult request);

    Optional<String> getPluginSystemUnionId(Long taskId);

    Optional<Long> getTaskIdByPluginSystemUnionId(String mappingId);

    Optional<String> getAwsRuleArnByTaskId(Long taskId);

    void addAwsRuleMapping(Long taskId, String awsRule);
}
