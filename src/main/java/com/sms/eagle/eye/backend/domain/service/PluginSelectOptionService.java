package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginSelectOptionEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.plugin.v1.SelectOption;
import java.util.List;

public interface PluginSelectOptionService extends IService<PluginSelectOptionEntity> {

    List<PluginSelectOptionEntity> getListByPluginId(Long pluginId);

    List<PluginSelectOptionResponse> getResponseByPluginId(Long pluginId);

    void saveFromRpcData(List<SelectOption> list, Long pluginId);
}
