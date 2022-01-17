package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.response.plugin.PluginMetadataResponse;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;

public interface PluginRpcService {

    RegisterResponse getRegisterResponseByTarget(String target);

    PluginMetadataResponse getMetadataResponseByTarget(String target);

}
