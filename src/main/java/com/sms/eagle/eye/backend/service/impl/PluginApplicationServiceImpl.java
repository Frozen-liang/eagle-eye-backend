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
import com.sms.eagle.eye.backend.request.plugin.PluginUpdateRequest;
import com.sms.eagle.eye.backend.response.plugin.AlertRuleResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
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

    /**
     * 添加插件
     *
     * <p>如果插件的 ScheduleBySelf 为true，则需要为系统告警级别
     * 与插件自身的告警级别设置映射关系，TODO 至少一条.
     *
     * <p>插件元数据中提供的 {@link RegisterResponse#getAlertsList()} 是所有的告警规则表单，
     * 添加插件时需要为不同告警级别绑定对应的告警规则表单.
     */
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

    /**
     * 修改插件的 级别映射关系 以及 绑定的告警规则表单.
     */
    @Override
    public boolean updatePlugin(PluginUpdateRequest request) {
        PluginEntity entity = pluginService.getEntityById(request.getId());
        if (entity.getScheduleBySelf()) {
            Assert.notEmpty(request.getAlarmLevelMapping(),
                "Please enter the plugin alarm level that corresponds to the system alarm level");
            pluginAlarmLevelMappingService.updateByRequest(request.getAlarmLevelMapping(), request.getId());
        }
        pluginAlertRuleService.updateByRequest(request.getAlertRule(), request.getId());
        return true;
    }

    /**
     * 获取插件详情.
     */
    @Override
    public PluginDetailResponse getPluginDetailById(Long id) {
        PluginEntity entity = pluginService.getEntityById(id);
        List<PluginAlertRuleFieldResponse> allPluginAlertFields = pluginAlertFieldService.getResponseByPluginId(id);
        Map<Integer, List<PluginAlertRuleEntity>> alertRuleMap = pluginAlertRuleService.getListByPluginId(id)
            .stream().collect(Collectors.groupingBy(PluginAlertRuleEntity::getAlarmLevel));
        Map<String, List<PluginAlertRuleFieldResponse>> alertFieldMap = allPluginAlertFields
            .stream().collect(Collectors.groupingBy(PluginAlertRuleFieldResponse::getKey));

        return PluginDetailResponse.builder()
            .name(entity.getName())
            .description(entity.getDescription())
            .version(entity.getVersion())
            .scheduleBySelf(entity.getScheduleBySelf())
            .options(pluginSelectOptionService.getResponseByPluginId(id))
            .fields(pluginConfigFieldService.getResponseByPluginId(id))
            .allAlerts(allPluginAlertFields)
            .alarmLevelMapping(pluginAlarmLevelMappingService.getResponseByPluginId(id))
            .alertRule(generateAlertRuleResponse(alertRuleMap, alertFieldMap))
            .build();
    }

    /**
     * 根据绑定的告警规则关系得到完整的告警规则表单数据.
     *
     * @param alertRuleMap key为告警级别，value是告警级别绑定的告警表单的key
     * @param alertFieldMap key是告警表单的key，value是告警表单详情（虽然是List，但只会有一个）
     */
    private List<AlertRuleResponse> generateAlertRuleResponse(Map<Integer, List<PluginAlertRuleEntity>> alertRuleMap,
        Map<String, List<PluginAlertRuleFieldResponse>> alertFieldMap) {
        List<AlertRuleResponse> list = new ArrayList<>();
        Iterator<Map.Entry<Integer, List<PluginAlertRuleEntity>>> iterator = alertRuleMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<PluginAlertRuleEntity>> next = iterator.next();
            List<String> ruleKeys = next.getValue().stream().map(PluginAlertRuleEntity::getAlertKey)
                .collect(Collectors.toList());
            list.add(AlertRuleResponse.builder()
                .alarmLevel(next.getKey())
                .alerts(getFieldByKeys(ruleKeys, alertFieldMap))
                .build());
        }
        return list;
    }

    private List<PluginAlertRuleFieldResponse> getFieldByKeys(List<String> ruleKeys,
        Map<String, List<PluginAlertRuleFieldResponse>> alertFieldMap) {
        List<PluginAlertRuleFieldResponse> list = new ArrayList<>();
        for (String key : ruleKeys) {
            list.addAll(alertFieldMap.get(key));
        }
        return list;
    }

    /**
     * 分页获取插件列表.
     */
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