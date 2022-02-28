package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
import com.sms.eagle.eye.backend.domain.service.PluginAlertFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginAlertRuleService;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.response.plugin.AlertRuleResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigRuleResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.service.PluginApplicationService;
import com.sms.eagle.eye.backend.service.PluginRpcService;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class PluginApplicationServiceImpl implements PluginApplicationService {

    private final PluginService pluginService;
    private final PluginRpcService pluginRpcService;
    private final PluginAlertRuleService pluginAlertRuleService;
    private final PluginAlertFieldService pluginAlertFieldService;
    private final PluginConfigFieldService pluginConfigFieldService;
    private final PluginSelectOptionService pluginSelectOptionService;
    private final PluginAlarmLevelMappingService pluginAlarmLevelMappingService;

    public PluginApplicationServiceImpl(PluginService pluginService,
        PluginRpcService pluginRpcService,
        PluginAlertRuleService pluginAlertRuleService,
        PluginAlertFieldService pluginAlertFieldService,
        PluginConfigFieldService pluginConfigFieldService,
        PluginSelectOptionService pluginSelectOptionService,
        PluginAlarmLevelMappingService pluginAlarmLevelMappingService) {
        this.pluginRpcService = pluginRpcService;
        this.pluginService = pluginService;
        this.pluginAlertRuleService = pluginAlertRuleService;
        this.pluginAlertFieldService = pluginAlertFieldService;
        this.pluginConfigFieldService = pluginConfigFieldService;
        this.pluginSelectOptionService = pluginSelectOptionService;
        this.pluginAlarmLevelMappingService = pluginAlarmLevelMappingService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addPlugin(PluginRequest request) {
        RegisterResponse registerResponse = pluginRpcService.getRegisterResponseByTarget(request.getUrl());
        Long pluginId = pluginService.savePluginAndReturnId(registerResponse, request.getUrl());

        if (registerResponse.getScheduleBySelf()) {
            Assert.notEmpty(request.getAlarmLevelMapping(),
                "Please enter the plugin alarm level that corresponds to the system alarm level");
            pluginAlarmLevelMappingService.updateByRequest(request.getAlarmLevelMapping(), pluginId);
        }
        pluginAlertRuleService.updateByRequest(request.getAlertRule(), pluginId);
        pluginAlertFieldService.saveFromRpcData(registerResponse.getAlertsList(), pluginId);
        pluginConfigFieldService.saveFromRpcData(registerResponse.getFieldsList(), pluginId);
        pluginSelectOptionService.saveFromRpcData(registerResponse.getOptionsList(), pluginId);
        return true;
    }

    @Override
    public PluginDetailResponse getPluginDetailById(Long id) {
        PluginEntity entity = pluginService.getEntityById(id);
        Map<String, List<PluginAlertRuleEntity>> alertRuleMap = pluginAlertRuleService.getListByPluginId(id)
            .stream().collect(Collectors.groupingBy(PluginAlertRuleEntity::getAlarmLevel));
        Map<String, List<PluginConfigRuleResponse>> alertFieldMap = pluginAlertFieldService.getResponseByPluginId(id)
            .stream().collect(Collectors.groupingBy(PluginConfigRuleResponse::getKey));

        return PluginDetailResponse.builder()
            .name(entity.getName())
            .description(entity.getDescription())
            .version(entity.getVersion())
            .scheduleBySelf(entity.getScheduleBySelf())
            .options(pluginSelectOptionService.getResponseByPluginId(id))
            .fields(pluginConfigFieldService.getResponseByPluginId(id))
            .alarmLevelMapping(pluginAlarmLevelMappingService.getResponseByPluginId(id))
            .alertRule(generateAlertRuleResponse(alertRuleMap, alertFieldMap))
            .build();
    }

    private List<AlertRuleResponse> generateAlertRuleResponse(Map<String, List<PluginAlertRuleEntity>> alertRuleMap,
        Map<String, List<PluginConfigRuleResponse>> alertFieldMap) {
        List<AlertRuleResponse> list = new ArrayList<>();
        Iterator<Map.Entry<String, List<PluginAlertRuleEntity>>> iterator = alertRuleMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<PluginAlertRuleEntity>> next = iterator.next();
            List<String> ruleKeys = next.getValue().stream().map(PluginAlertRuleEntity::getAlertKey)
                .collect(Collectors.toList());
            list.add(AlertRuleResponse.builder()
                .alarmLevel(next.getKey())
                .alerts(getFieldByKeys(ruleKeys, alertFieldMap))
                .build());
        }
        return list;
    }

    private List<PluginConfigRuleResponse> getFieldByKeys(List<String> ruleKeys,
        Map<String, List<PluginConfigRuleResponse>> alertFieldMap) {
        List<PluginConfigRuleResponse> list = new ArrayList<>();
        for (String key : ruleKeys) {
            list.addAll(alertFieldMap.get(key));
        }
        return list;
    }

    @Override
    public CustomPage<PluginResponse> page(PluginQueryRequest request) {
        return new CustomPage<>(pluginService.getPage(request));
    }

    /**
     * TODO 完善逻辑
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