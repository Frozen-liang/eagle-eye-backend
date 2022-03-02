package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginAlarmLevelMappingEntity;
import com.sms.eagle.eye.backend.request.plugin.AlarmLevelMappingRequest;
import com.sms.eagle.eye.backend.response.plugin.AlarmLevelMappingResponse;
import java.util.List;

public interface PluginAlarmLevelMappingService extends IService<PluginAlarmLevelMappingEntity> {

    void updateByRequest(List<AlarmLevelMappingRequest> request, Long pluginId);

    List<AlarmLevelMappingResponse> getResponseByPluginId(Long pluginId);
}
