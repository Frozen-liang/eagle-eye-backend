package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import java.util.List;
import java.util.Optional;

public interface ThirdPartyMappingService extends IService<ThirdPartyMappingEntity> {

    void addPluginSystemUnionIdMapping(Long taskId, String mappingId);

    Optional<String> getPluginSystemUnionId(Long taskId);

    Optional<Long> getTaskIdByPluginSystemUnionId(String mappingId);

    Optional<String> getAwsRuleArnByTaskAlertRuleId(Long taskAlertRuleId);

    void addAwsRuleMapping(Long taskAlertRuleId, String awsRule);

    void addAwsRuleTargetMapping(Long taskAlertRuleId, String awsRuleTargetId);

    void removeAwsRuleTargetMapping(Long taskId);

    List<String> getAwsRuleTargetList(Long taskId);

}
