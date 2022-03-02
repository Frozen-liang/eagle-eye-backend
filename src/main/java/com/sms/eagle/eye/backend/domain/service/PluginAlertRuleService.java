package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.request.plugin.AlertRuleRequest;
import java.util.List;

public interface PluginAlertRuleService extends IService<PluginAlertRuleEntity> {

    void updateByRequest(List<AlertRuleRequest> requests, Long pluginId);

    List<PluginAlertRuleEntity> getListByPluginId(Long pluginId);
}
