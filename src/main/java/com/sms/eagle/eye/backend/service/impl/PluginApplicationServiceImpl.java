package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.ALARM_MAPPING_CAN_NOT_BE_REPEATED;
import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_HAS_ALREADY_EXIST;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
import com.sms.eagle.eye.backend.domain.service.PluginAlertFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginAlertRuleService;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.event.DisablePluginEvent;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class PluginApplicationServiceImpl implements PluginApplicationService, ApplicationContextAware {

    private ApplicationContext applicationContext;

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
     * ????????????
     *
     * <p>?????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????.
     *
     * <p>??????????????? ScheduleBySelf ???true?????????????????????????????????
     * ???????????????????????????????????????????????????TODO ????????????.
     *
     * <p>??????????????????????????? {@link RegisterResponse#getAlertsList()} ?????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addPlugin(PluginRequest request) {
        RegisterResponse registerResponse = pluginRpcService.getRegisterResponseByTarget(request.getUrl());
        Optional<PluginEntity> pluginEntityOptional = pluginService.getByName(registerResponse.getName());
        pluginEntityOptional.ifPresentOrElse(plugin -> {
            if (registerResponse.getVersion() > plugin.getVersion()) {
                // TODO ??????
            } else {
                if (!StringUtils.equals(plugin.getUrl().trim(), request.getUrl().trim())) {
                    pluginService.updatePluginUrlOrVersion(plugin.getId(), null, request.getUrl().trim());
                } else {
                    throw new EagleEyeException(PLUGIN_HAS_ALREADY_EXIST);
                }
            }
        }, () -> {
            Long pluginId = pluginService.savePluginAndReturnId(registerResponse, request.getUrl());
            if (registerResponse.getScheduleBySelf()) {
                Assert.notEmpty(request.getAlarmLevelMapping(),
                    "Please enter the plugin alarm level that corresponds to the system alarm level");
                if (request.getAlarmLevelMapping().stream().distinct().count()
                    < request.getAlarmLevelMapping().size()) {
                    throw new EagleEyeException(ALARM_MAPPING_CAN_NOT_BE_REPEATED);
                }
                pluginAlarmLevelMappingService.updateByRequest(request.getAlarmLevelMapping(), pluginId);
            }
            pluginAlertRuleService.updateByRequest(request.getAlertRule(), pluginId);
            pluginAlertFieldService.saveFromRpcData(registerResponse.getAlertsList(), pluginId);
            pluginConfigFieldService.saveFromRpcData(registerResponse.getFieldsList(), pluginId);
            pluginSelectOptionService.saveFromRpcData(registerResponse.getOptionsList(), pluginId);
        });
        return true;
    }

    /**
     * ??????????????? ?????????????????? ?????? ???????????????????????????.
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
     * ??????????????????.
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
     * ????????????????????????????????????????????????????????????????????????.
     *
     * @param alertRuleMap  key??????????????????value???????????????????????????????????????key
     * @param alertFieldMap key??????????????????key???value?????????????????????????????????List????????????????????????
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
     * ????????????????????????.
     */
    @Override
    public CustomPage<PluginResponse> page(PluginQueryRequest request) {
        return new CustomPage<>(pluginService.getPage(request));
    }

    /**
     * TODO ????????????
     * ???????????? ???????????????????????????????????????.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deletePlugin(Long pluginId) {
        applicationContext.publishEvent(DisablePluginEvent.builder().pluginId(pluginId).build());
        pluginService.deletePlugin(pluginId);
        return true;
    }

    @Override
    public boolean enablePlugin(Long pluginId) {
        pluginService.updatePluginStatus(pluginId, Boolean.TRUE);
        return true;
    }

    /**
     * ????????????????????????????????????????????????????????????.
     */
    @Override
    public boolean disablePlugin(Long pluginId) {
        applicationContext.publishEvent(DisablePluginEvent.builder().pluginId(pluginId).build());
        pluginService.updatePluginStatus(pluginId, Boolean.FALSE);
        return true;
    }

    /**
     * ?????????????????????.
     * <li>?????? scheduleBySelf ??? true????????? {@link com.sms.eagle.eye.backend.domain.entity.PluginAlarmLevelMappingEntity}
     * ???????????????????????????????????????????????????
     * <li>?????????false????????????????????????
     */
    @Override
    public List<Integer> getAllAlarmLevel(Long pluginId) {
        PluginEntity entity = pluginService.getEntityById(pluginId);
        if (Objects.equals(entity.getScheduleBySelf(), Boolean.TRUE)) {
            return pluginAlarmLevelMappingService.getAlarmLevelByPluginId(pluginId);
        } else {
            return AlarmLevel.getValueList();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}