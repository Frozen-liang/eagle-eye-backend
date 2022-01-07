package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import java.util.List;

public interface PluginConfigFieldService extends IService<PluginConfigFieldEntity> {

    List<PluginConfigFieldEntity> getListByPluginId(Long pluginId);

    List<PluginConfigFieldResponse> getResponseByPluginId(Long pluginId);

    void saveFromRpcData(List<ConfigField> list, Long pluginId);
}
