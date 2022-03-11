package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.domain.entity.PluginAlarmLevelMappingEntity;
import com.sms.eagle.eye.backend.domain.service.impl.PluginAlarmLevelMappingServiceImpl;
import com.sms.eagle.eye.backend.request.plugin.AlarmLevelMappingRequest;
import com.sms.eagle.eye.backend.response.plugin.AlarmLevelMappingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PluginAlarmLevelMappingServiceTest {

    private final PluginAlarmLevelMappingService pluginAlarmLevelMappingService =
            spy(new PluginAlarmLevelMappingServiceImpl());
    private final PluginAlarmLevelMappingEntity alarmLevelMappingEntity = mock(PluginAlarmLevelMappingEntity.class);

    private static final String LEVEL = "LEVEL";
    private static final Integer INTEGER = 1;
    private static final Long ID = 1L;
    @Test
    @DisplayName("Test the updateTemplate method in the Plugin AlarmLevel Mapping Service")
    public void updateByRequest_test(){
        doReturn(Boolean.TRUE).when(pluginAlarmLevelMappingService).remove(any());
        AlarmLevelMappingRequest alarmLevelMappingRequest = AlarmLevelMappingRequest.builder()
                .systemLevel(INTEGER).mappingLevel(LEVEL).build();
        doReturn(Boolean.TRUE).when(pluginAlarmLevelMappingService).saveBatch(any());
        pluginAlarmLevelMappingService.updateByRequest(List.of(alarmLevelMappingRequest),ID);
        verify(pluginAlarmLevelMappingService,times(1)).remove(any());
        verify(pluginAlarmLevelMappingService,times(1)).saveBatch(any());
    }

    @Test
    @DisplayName("Test the getResponseByPluginId method in the Plugin AlarmLevel Mapping Service")
    public void getResponseByPluginId_test(){
        List<AlarmLevelMappingResponse> list = mock(List.class);
        doReturn(list).when(pluginAlarmLevelMappingService).list(any(Wrapper.class));
        when(alarmLevelMappingEntity.getMappingLevel()).thenReturn(LEVEL);
        assertThat(pluginAlarmLevelMappingService.getResponseByPluginId(ID)).hasSize(0);
    }

    @Test
    @DisplayName("Test the getMappingLevelByPluginIdAndSystemLevel method in the Plugin AlarmLevel Mapping Service")
    public void getMappingLevelByPluginIdAndSystemLevel_test(){
        doReturn(null).when(pluginAlarmLevelMappingService).getOne(any());
        assertThat(pluginAlarmLevelMappingService.getMappingLevelByPluginIdAndSystemLevel(ID,INTEGER)).isNotNull();
    }
}
