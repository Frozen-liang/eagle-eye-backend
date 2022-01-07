package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.backend.service.PluginApplicationService;
import com.sms.eagle.eye.backend.service.PluginRpcService;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PluginApplicationServiceImpl implements PluginApplicationService {

    private final PluginService pluginService;
    private final PluginRpcService pluginRpcService;
    private final PluginConfigFieldService pluginConfigFieldService;
    private final PluginSelectOptionService pluginSelectOptionService;

    public PluginApplicationServiceImpl(PluginService pluginService,
        PluginRpcService pluginRpcService,
        PluginConfigFieldService pluginConfigFieldService,
        PluginSelectOptionService pluginSelectOptionService) {
        this.pluginRpcService = pluginRpcService;
        this.pluginService = pluginService;
        this.pluginConfigFieldService = pluginConfigFieldService;
        this.pluginSelectOptionService = pluginSelectOptionService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addPlugin(PluginRequest request) {
        RegisterResponse registerResponse = pluginRpcService.getRegisterResponseByTarget(request.getUrl());
        Long pluginId = pluginService.savePluginAndReturnId(registerResponse, request.getUrl());
        pluginConfigFieldService.saveFromRpcData(registerResponse.getFieldsList(), pluginId);
        pluginSelectOptionService.saveFromRpcData(registerResponse.getOptionsList(), pluginId);
        return true;
    }

    @Override
    public PluginDetailResponse getPluginDetailById(Long id) {
        List<PluginSelectOptionResponse> selectOptions = pluginSelectOptionService.getResponseByPluginId(id);
        List<PluginConfigFieldResponse> configFields = pluginConfigFieldService.getResponseByPluginId(id);
        return PluginDetailResponse.builder()
            .options(selectOptions)
            .fields(configFields)
            .build();
    }

    @Override
    public CustomPage<PluginResponse> page(PluginQueryRequest request) {
        return new CustomPage<>(pluginService.getPage(request));
    }

    /**
     * 删除插件 停止并删除使用该插件的任务.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deletePlugin(Long pluginId) {
        pluginService.deletePlugin(pluginId);
        return true;
    }

    @Override
    public boolean enablePlugin(Long pluginId) {
        pluginService.updatePluginStatus(pluginId, Boolean.TRUE);
        return true;
    }

    @Override
    public boolean disablePlugin(Long pluginId) {
        pluginService.updatePluginStatus(pluginId, Boolean.FALSE);
        return true;
    }

}