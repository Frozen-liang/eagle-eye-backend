package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import java.util.Optional;

public interface ThirdPartyMappingService extends IService<ThirdPartyMappingEntity> {

    void addPluginSystemUnionIdMapping(Long taskId, String mappingId);

    Optional<String> getPluginSystemUnionId(Long taskId);

    Optional<Long> getTaskIdByPluginSystemUnionId(String mappingId);

    Optional<String> getAwsRuleArnByTaskId(Long taskId);

    void addAwsRuleMapping(Long taskId, String awsRule);
}
