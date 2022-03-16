package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import java.util.List;

public interface PluginAlertFieldService extends IService<PluginAlertFieldEntity> {

    void saveFromRpcData(List<ConfigField> list, Long pluginId);

    List<PluginAlertRuleFieldResponse> getResponseByPluginId(Long pluginId);

    List<PluginAlertFieldEntity> getListByPluginId(Long pluginId);
}
