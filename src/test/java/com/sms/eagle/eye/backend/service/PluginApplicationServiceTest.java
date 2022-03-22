package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.service.impl.PluginApplicationServiceImpl;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

public class PluginApplicationServiceTest {

    private static final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private static final PluginService pluginService = mock(PluginService.class);
    private static final PluginRpcService RpcService = mock(PluginRpcService.class);
    private static final PluginAlertRuleService AlertRuleService = mock(PluginAlertRuleService.class);
    private static final PluginAlertFieldService AlertFieldService = mock(PluginAlertFieldService.class);
    private static final PluginConfigFieldService ConfigFieldService = mock(PluginConfigFieldService.class);
    private static final PluginSelectOptionService SelectOptionService = mock(PluginSelectOptionService.class);
    private static final PluginAlarmLevelMappingService AlarmLevelMappingService =
        mock(PluginAlarmLevelMappingService.class);

    private final PluginRequest pluginRequest = mock(PluginRequest.class);
    private final RegisterResponse registerResponse = mock(RegisterResponse.class);
    private final PluginEntity entity = mock(PluginEntity.class);
    private final PluginQueryRequest pluginQueryRequest = mock(PluginQueryRequest.class);

    private static final PluginApplicationServiceImpl pluginApplicationService =
            new PluginApplicationServiceImpl(pluginService, RpcService, AlertRuleService, AlertFieldService,
                    ConfigFieldService, SelectOptionService, AlarmLevelMappingService);

    private static final Long ID = 1L;
    private static final String VALUE = "VALUE";
    private static final Integer INTEGER = 1;

    @BeforeAll
    public static void init() {
        pluginApplicationService.setApplicationContext(applicationContext);
    }

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
