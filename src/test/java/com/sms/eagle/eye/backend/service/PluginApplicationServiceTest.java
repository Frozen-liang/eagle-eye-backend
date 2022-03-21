package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.service.*;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.response.plugin.AlertRuleResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
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
        // 请求构建参数
        String url = pluginRequest.getUrl();
        // mock
        when(RpcService.getRegisterResponseByTarget(url)).thenReturn(registerResponse);
        when(pluginService.savePluginAndReturnId(registerResponse, url)).thenReturn(ID);
        when(registerResponse.getScheduleBySelf()).thenReturn(Boolean.FALSE);
        when(pluginRequest.getAlarmLevelMapping()).thenReturn(Collections.emptyList());
        doNothing().when(AlarmLevelMappingService).updateByRequest(any(), any());
        doNothing().when(AlertRuleService).updateByRequest(any(), any());
        doNothing().when(AlertFieldService).saveFromRpcData(any(), any());
        doNothing().when(ConfigFieldService).saveFromRpcData(any(), any());
        doNothing().when(SelectOptionService).saveFromRpcData(any(), any());
        // 执行
        boolean addPlugin = pluginApplicationService.addPlugin(pluginRequest);
        // 验证
        assertThat(addPlugin).isTrue();
    }

    @Test
    @DisplayName("Test the getPluginDetailById method in the Plugin Application Service")
    public void getPluginDetailById_test() {
        // mock
        when(pluginService.getEntityById(ID)).thenReturn(entity);
        when(AlertRuleService.getListByPluginId(ID)).thenReturn(Collections.emptyList());
        when(AlertFieldService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        when(entity.getName()).thenReturn(VALUE);
        when(entity.getDescription()).thenReturn(VALUE);
        when(entity.getVersion()).thenReturn(INTEGER);
        when(entity.getScheduleBySelf()).thenReturn(Boolean.TRUE);
        when(SelectOptionService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        when(ConfigFieldService.getResponseByPluginId(ID)).thenReturn(Collections.emptyList());
        // 执行
        PluginDetailResponse pluginDetailById = pluginApplicationService.getPluginDetailById(ID);
        // 验证
        assertThat(pluginDetailById).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the Plugin Application Service")
    public void page() {
        // mock
        IPage<PluginResponse> page = mock(IPage.class);
        List<PluginResponse> list = mock(List.class);
        doReturn(list).when(page).getRecords();
        when(pluginService.getPage(pluginQueryRequest)).thenReturn(page);
        // 执行
        CustomPage<PluginResponse> result = pluginApplicationService.page(pluginQueryRequest);
        //
        assertThat(page).isNotNull();
        assertThat(page.getRecords()).isEqualTo(list);
    }

    @Test
    @DisplayName("Test the deletePlugin method in the Plugin Application Service")
    public void deletePlugin_test() {
        // mock
        doNothing().when(pluginService).deletePlugin(ID);
        // 执行
        boolean deletePlugin = pluginApplicationService.deletePlugin(ID);
        // 验证
        assertThat(deletePlugin).isTrue();
    }

    @Test
    @DisplayName("Test the enablePlugin method in the Plugin Application Service")
    public void enablePlugin_test() {
        // mock
        doNothing().when(pluginService).updatePluginStatus(ID,Boolean.TRUE);
        // 执行
        boolean enablePlugin = pluginApplicationService.enablePlugin(ID);
        // 验证
        assertThat(enablePlugin).isTrue();
    }

    @Test
    @DisplayName("Test the disablePlugin method in the Plugin Application Service")
    public void disablePlugin_test() {
        // mock
        doNothing().when(pluginService).updatePluginStatus(ID,Boolean.FALSE);
        // 执行
        boolean disablePlugin = pluginApplicationService.disablePlugin(ID);
        // 验证
        assertThat(disablePlugin).isTrue();
    }
}
