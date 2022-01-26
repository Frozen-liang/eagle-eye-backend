package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.common.enums.ThirdPartyType;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.ThirdPartyMappingMapper;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class ThirdPartyMappingServiceImpl extends ServiceImpl<ThirdPartyMappingMapper, ThirdPartyMappingEntity>
    implements ThirdPartyMappingService {

    @Override
    public void addPluginSystemUnionIdMapping(Long taskId, String mappingId) {
        save(ThirdPartyMappingEntity.builder()
            .taskId(taskId)
            .mappingId(mappingId)
            .type(ThirdPartyType.PLUGIN_SYSTEM_UNION_ID.getValue())
            .build());
    }

    @Override
    public Optional<String> getPluginSystemUnionId(Long taskId) {
        return getBaseMapper().getMappingIdByTaskId(taskId, ThirdPartyType.PLUGIN_SYSTEM_UNION_ID.getValue());
    }

    @Override
    public Optional<Long> getTaskIdByPluginSystemUnionId(String mappingId) {
        return getBaseMapper().getTaskIdByMappingId(mappingId, ThirdPartyType.PLUGIN_SYSTEM_UNION_ID.getValue());
    }

    @Override
    public Optional<String> getAwsRuleArnByTaskId(Long taskId) {
        return getBaseMapper().getMappingIdByTaskId(taskId, ThirdPartyType.AWS_EVENT_BRIDGE_RULE.getValue());
    }

    @Override
    public void addAwsRuleMapping(Long taskId, String awsRule) {
        save(ThirdPartyMappingEntity.builder()
            .taskId(taskId)
            .mappingId(awsRule)
            .type(ThirdPartyType.AWS_EVENT_BRIDGE_RULE.getValue())
            .build());
    }

    @Override
    public void addAwsRuleTargetMapping(Long taskId, String awsRuleTargetId) {
        save(ThirdPartyMappingEntity.builder()
            .taskId(taskId)
            .mappingId(awsRuleTargetId)
            .type(ThirdPartyType.AWS_EVENT_BRIDGE_RULE_TARGET.getValue())
            .build());
    }

    @Override
    public void removeAwsRuleTargetMapping(Long taskId) {
        remove(Wrappers.<ThirdPartyMappingEntity>lambdaQuery()
            .eq(ThirdPartyMappingEntity::getTaskId, taskId)
            .eq(ThirdPartyMappingEntity::getType, ThirdPartyType.AWS_EVENT_BRIDGE_RULE_TARGET.getValue()));
    }

    @Override
    public List<String> getAwsRuleTargetList(Long taskId) {
        List<ThirdPartyMappingEntity> list = list(Wrappers.<ThirdPartyMappingEntity>lambdaQuery()
            .eq(ThirdPartyMappingEntity::getTaskId, taskId)
            .eq(ThirdPartyMappingEntity::getType, ThirdPartyType.AWS_EVENT_BRIDGE_RULE_TARGET.getValue()));
        return CollectionUtils.isEmpty(list) ? Collections.emptyList()
            : list.stream().map(ThirdPartyMappingEntity::getMappingId).collect(Collectors.toList());
    }
}




