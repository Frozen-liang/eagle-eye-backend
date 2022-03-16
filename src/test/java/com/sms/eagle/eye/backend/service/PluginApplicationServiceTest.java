package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.service.*;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.response.plugin.AlertRuleResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
import com.sms.eagle.eye.backend.service.impl.PluginApplicationServiceImpl;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class PluginApplicationServiceTest {

    private final PluginService pluginService = mock(PluginService.class);
    private final PluginRpcService RpcService = mock(PluginRpcService.class);
    private final PluginAlertRuleService AlertRuleService = mock(PluginAlertRuleService.class);
    private final PluginAlertFieldService AlertFieldService = mock(PluginAlertFieldService.class);
    private final PluginConfigFieldService ConfigFieldService = mock(PluginConfigFieldService.class);
    private final PluginSelectOptionService SelectOptionService = mock(PluginSelectOptionService.class);
    private final PluginAlarmLevelMappingService AlarmLevelMappingService = mock(PluginAlarmLevelMappingService.class);
    private final PluginRequest pluginRequest = mock(PluginRequest.class);
    private final RegisterResponse registerResponse = mock(RegisterResponse.class);
    private final PluginEntity entity = mock(PluginEntity.class);
    private final PluginQueryRequest pluginQueryRequest = mock(PluginQueryRequest.class);

    private final PluginApplicationService pluginApplicationService =
            new PluginApplicationServiceImpl(pluginService, RpcService, AlertRuleService, AlertFieldService,
                    ConfigFieldService, SelectOptionService, AlarmLevelMappingService);

    private static final Long ID = 1L;
    private static final String VALUE = "VALUE";
    private static final Integer INTEGER = 1;

    @Test
    @DisplayName("Test the page method in the Plugin Application Service")
    public void addPlugin_test() {
        String url = pluginRequest.getUrl();
        when(RpcService.getRegisterResponseByTarget(url)).thenReturn(registerResponse);
        when(pluginService.savePluginAndReturnId(registerResponse, url)).thenReturn(ID);
        when(registerResponse.getScheduleBySelf()).thenReturn(Boolean.FALSE);
        when(pluginRequest.getAlarmLevelMapping()).thenReturn(Collections.emptyList());
        doNothing().when(AlarmLevelMappingService).updateByRequest(any(), any());
        doNothing().when(AlertRuleService).updateByRequest(any(), any());
        doNothing().when(AlertFieldService).saveFromRpcData(any(), any());
        doNothing().when(ConfigFieldService).saveFromRpcData(any(), any());
        doNothing().when(SelectOptionService).saveFromRpcData(any(), any());
        assertThat(pluginApplicationService.addPlugin(pluginRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the getPluginDetailById method in the Plugin Application Service")
    public void getPluginDetailById_test() {
        when(pluginService.getEntityById(ID)).thenReturn(entity);
        when(AlertRuleService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(AlertFieldService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        when(entity.getName()).thenReturn(VALUE);
        when(entity.getDescription()).thenReturn(VALUE);
        when(entity.getVersion()).thenReturn(INTEGER);
        when(entity.getScheduleBySelf()).thenReturn(Boolean.TRUE);
        when(SelectOptionService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        when(ConfigFieldService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());

        Map<Integer, List<PluginAlertRuleEntity>> alertRuleMap = AlertRuleService.getListByPluginId(ID)
                .stream().collect(Collectors.groupingBy(PluginAlertRuleEntity::getAlarmLevel));
        Map<String, List<PluginAlertRuleFieldResponse>> alertFieldMap = AlertFieldService.getResponseByPluginId(ID)
                .stream().collect(Collectors.groupingBy(PluginAlertRuleFieldResponse::getKey));
        when(generateAlertRuleResponse(alertRuleMap, alertFieldMap)).thenReturn(Collections.emptyList());

        assertThat(pluginApplicationService.getPluginDetailById(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the Plugin Application Service")
    public void page() {
        when(pluginService.getPage(pluginQueryRequest)).thenReturn(new Page<>());
        assertThat(pluginApplicationService.page(pluginQueryRequest)).isNotNull();
    }

    @Test
    @DisplayName("Test the deletePlugin method in the Plugin Application Service")
    public void deletePlugin_test() {
        doNothing().when(pluginService).deletePlugin(ID);
        assertThat(pluginApplicationService.deletePlugin(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the enablePlugin method in the Plugin Application Service")
    public void enablePlugin_test() {
        doNothing().when(pluginService).updatePluginStatus(ID,Boolean.TRUE);
        assertThat(pluginApplicationService.enablePlugin(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the disablePlugin method in the Plugin Application Service")
    public void disablePlugin_test() {
        doNothing().when(pluginService).updatePluginStatus(ID,Boolean.FALSE);
        assertThat(pluginApplicationService.disablePlugin(ID)).isTrue();
    }

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
}
