package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;

public interface PluginApplicationService {

    boolean addPlugin(PluginRequest request);

    PluginDetailResponse getPluginDetailById(Long id);

    CustomPage<PluginResponse> page(PluginQueryRequest request);

    boolean deletePlugin(Long pluginId);

    boolean enablePlugin(Long pluginId);

    boolean disablePlugin(Long pluginId);

}
