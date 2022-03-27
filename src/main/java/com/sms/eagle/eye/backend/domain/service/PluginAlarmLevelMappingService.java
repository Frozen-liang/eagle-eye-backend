package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginAlarmLevelMappingEntity;
import com.sms.eagle.eye.backend.request.plugin.AlarmLevelMappingRequest;
import com.sms.eagle.eye.backend.response.plugin.AlarmLevelMappingResponse;
import java.util.List;
import java.util.Optional;

public interface PluginAlarmLevelMappingService extends IService<PluginAlarmLevelMappingEntity> {

    void updateByRequest(List<AlarmLevelMappingRequest> request, Long pluginId);

    List<AlarmLevelMappingResponse> getResponseByPluginId(Long pluginId);

    Optional<String> getMappingLevelByPluginIdAndSystemLevel(Long pluginId, Integer systemLevel);

    Optional<Integer> getSystemLevelByPluginIdAndMappingLevel(Long pluginId, String mappingLevel);

    List<Integer> getAlarmLevelByPluginId(Long pluginId);
}
