package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;

public interface PluginService extends IService<PluginEntity> {

    IPage<PluginResponse> getPage(PluginQueryRequest request);

    Long savePluginAndReturnId(RegisterResponse registerResponse, String url);

    List<IdNameResponse<Long>> getList();

    void deletePlugin(Long pluginId);

    void updatePluginStatus(Long pluginId, Boolean enabled);

    PluginEntity getEntityById(Long pluginId);
}
