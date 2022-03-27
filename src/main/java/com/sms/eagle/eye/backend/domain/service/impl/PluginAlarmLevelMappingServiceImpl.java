package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.PluginAlarmLevelMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginAlarmLevelMappingMapper;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
import com.sms.eagle.eye.backend.request.plugin.AlarmLevelMappingRequest;
import com.sms.eagle.eye.backend.response.plugin.AlarmLevelMappingResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class PluginAlarmLevelMappingServiceImpl extends
    ServiceImpl<PluginAlarmLevelMappingMapper, PluginAlarmLevelMappingEntity>
    implements PluginAlarmLevelMappingService {

    @Override
    public void updateByRequest(List<AlarmLevelMappingRequest> request, Long pluginId) {
        remove(Wrappers.<PluginAlarmLevelMappingEntity>lambdaQuery()
            .eq(PluginAlarmLevelMappingEntity::getPluginId, pluginId));
        saveBatch(request.stream().map(alarmLevelMappingRequest -> PluginAlarmLevelMappingEntity
            .builder().pluginId(pluginId)
            .systemLevel(alarmLevelMappingRequest.getSystemLevel())
            .mappingLevel(alarmLevelMappingRequest.getMappingLevel())
            .build()).collect(Collectors.toList()));
    }

    @Override
    public List<AlarmLevelMappingResponse> getResponseByPluginId(Long pluginId) {
        return list(Wrappers.<PluginAlarmLevelMappingEntity>lambdaQuery()
            .eq(PluginAlarmLevelMappingEntity::getPluginId, pluginId)).stream()
            .map(entity -> AlarmLevelMappingResponse.builder()
                .systemLevel(entity.getSystemLevel())
                .mappingLevel(entity.getMappingLevel())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public Optional<String> getMappingLevelByPluginIdAndSystemLevel(Long pluginId, Integer systemLevel) {
        return Optional.ofNullable(getOne(Wrappers.<PluginAlarmLevelMappingEntity>lambdaQuery()
            .eq(PluginAlarmLevelMappingEntity::getPluginId, pluginId)
            .eq(PluginAlarmLevelMappingEntity::getSystemLevel, systemLevel)))
            .map(PluginAlarmLevelMappingEntity::getMappingLevel);
    }

    @Override
    public Optional<Integer> getSystemLevelByPluginIdAndMappingLevel(Long pluginId, String mappingLevel) {
        return Optional.ofNullable(getOne(Wrappers.<PluginAlarmLevelMappingEntity>lambdaQuery()
                .eq(PluginAlarmLevelMappingEntity::getPluginId, pluginId)
                .eq(PluginAlarmLevelMappingEntity::getMappingLevel, mappingLevel)))
            .map(PluginAlarmLevelMappingEntity::getSystemLevel);
    }

    @Override
    public List<Integer> getAlarmLevelByPluginId(Long pluginId) {
        return list(Wrappers.<PluginAlarmLevelMappingEntity>lambdaQuery()
            .eq(PluginAlarmLevelMappingEntity::getPluginId, pluginId)).stream()
            .map(PluginAlarmLevelMappingEntity::getSystemLevel)
            .collect(Collectors.toList());
    }
}




