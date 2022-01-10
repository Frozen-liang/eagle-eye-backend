package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.common.enums.ThirdPartyType;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.ThirdPartyMappingMapper;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class ThirdPartyMappingServiceImpl extends ServiceImpl<ThirdPartyMappingMapper, ThirdPartyMappingEntity>
    implements ThirdPartyMappingService {

    @Override
    public void addPluginSystemUnionIdMapping(LambdaInvokeResult request) {
        save(ThirdPartyMappingEntity.builder()
            .taskId(request.getTaskId())
            .mappingId(request.getMappingId())
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
}




