package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.convert.PluginConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginMapper;
import com.sms.eagle.eye.backend.domain.service.impl.PluginServiceImpl;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class PluginServiceTest {

    PluginMapper pluginMapper = mock(PluginMapper.class);
    PluginConverter pluginConverter = mock(PluginConverter.class);
    PluginServiceImpl pluginService = spy(new PluginServiceImpl(pluginConverter));

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    void getPage_test() {
        IPage<PluginResponse> result = mock(IPage.class);
        Page<PluginEntity> page = mock(Page.class);
        PluginQueryRequest request = mock(PluginQueryRequest.class);
        when(request.getPageInfo()).thenReturn(page);
        doReturn(pluginMapper).when(pluginService).getBaseMapper();
        when(pluginMapper.getPage(page, request)).thenReturn(result);
        assertThat(pluginService.getPage(request)).isEqualTo(result);
    }

    @Test
    void savePluginAndReturnId_test() {
        Long pluginId = 1L;
        String url = "url";
        String username = "username";
        UserInfo userInfo = mock(UserInfo.class);
        when(userInfo.getUsername()).thenReturn(username);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);
        RegisterResponse registerResponse = mock(RegisterResponse.class);
        PluginEntity pluginEntity = mock(PluginEntity.class);
        when(pluginConverter.rpcResponseToEntity(registerResponse, url, username)).thenReturn(pluginEntity);
        doReturn(true).when(pluginService).save(pluginEntity);
        when(pluginEntity.getId()).thenReturn(pluginId);
        assertThat(pluginService.savePluginAndReturnId(registerResponse, url)).isEqualTo(pluginId);
    }

    @Test
    void getList_test() {
        List<IdNameResponse<Long>> list = mock(List.class);
        doReturn(pluginMapper).when(pluginService).getBaseMapper();
        when(pluginMapper.getList()).thenReturn(list);
        assertThat(pluginService.getList()).isEqualTo(list);
    }
}