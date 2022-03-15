package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.ThirdPartyMappingMapper;
import com.sms.eagle.eye.backend.domain.service.impl.ThirdPartyMappingServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThirdPartyMappingServiceTest {

    private final ThirdPartyMappingService service = spy(new ThirdPartyMappingServiceImpl());
    private final ThirdPartyMappingMapper thirdPartyMapping = mock(ThirdPartyMappingMapper.class);
    private final ThirdPartyMappingEntity entity = mock(ThirdPartyMappingEntity.class);

    private static final Long ID = 1L;
    private static final Long LONG = 1L;
    private static final String MAPPINGID = "MAPPINGID";
    private static final String AWSRULETARGETID = "AWSRULETARGETID";
    private static final String NAME = "NAME";
    private static final Integer INTEGER = 1;

    @Test
    @DisplayName("Test the addPluginSystemUnionIdMapping method in the Task Task Group Service")
    public void addPluginSystemUnionIdMapping_test() {
        doReturn(true).when(service).save(any(ThirdPartyMappingEntity.class));
        service.addPluginSystemUnionIdMapping(ID,MAPPINGID);
        verify(service).save(any());
    }

    @Test
    @DisplayName("Test the getPluginSystemUnionId method in the Task Task Group Service")
    public void getPluginSystemUnionId_test() {
        doReturn(thirdPartyMapping).when(service).getBaseMapper();
        when(thirdPartyMapping.getSystemIdByMappingId(MAPPINGID,INTEGER)).thenReturn(Optional.ofNullable(LONG));
        assertThat(service.getPluginSystemUnionId(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the getTaskIdByPluginSystemUnionId method in the Task Task Group Service")
    public void getTaskIdByPluginSystemUnionId_test() {
        doReturn(thirdPartyMapping).when(service).getBaseMapper();
        when(thirdPartyMapping.getSystemIdByMappingId(MAPPINGID,INTEGER)).thenReturn(Optional.ofNullable(LONG));
        assertThat(service.getPluginSystemUnionId(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the getAwsRuleArnByTaskAlertRuleId method in the Task Task Group Service")
    public void getAwsRuleArnByTaskAlertRuleId_test() {
        doReturn(thirdPartyMapping).when(service).getBaseMapper();
        when(thirdPartyMapping.getSystemIdByMappingId(MAPPINGID,INTEGER)).thenReturn(Optional.ofNullable(LONG));
        assertThat(service.getAwsRuleArnByTaskAlertRuleId(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the addAwsRuleMapping method in the Task Task Group Service")
    public void addAwsRuleMapping_test() {
        doReturn(Boolean.TRUE).when(service).save(any(ThirdPartyMappingEntity.class));
        service.addAwsRuleMapping(ID,AWSRULETARGETID);
        verify(service).save(any());
    }

    @Test
    @DisplayName("Test the addAwsRuleTargetMapping method in the Task Task Group Service")
    public void addAwsRuleTargetMapping_test() {
        doReturn(Boolean.TRUE).when(service).save(any(ThirdPartyMappingEntity.class));
        service.addAwsRuleTargetMapping(ID,AWSRULETARGETID);
        verify(service).save(any());
    }

    @Test
    @DisplayName("Test the removeAwsRuleTargetMapping method in the Task Task Group Service")
    public void removeAwsRuleTargetMapping_test() {
        doReturn(true).when(service).remove(any(Wrapper.class));
        service.removeAwsRuleTargetMapping(ID);
        verify(service).remove(any());
    }

    @Test
    @DisplayName("Test the getAwsRuleTargetList method in the Task Task Group Service")
    public void getAwsRuleTargetList_test() {
        List<ThirdPartyMappingEntity> list = mock(List.class);
        list.add(entity);
        doReturn(list).when(service).list(any(Wrapper.class));
        assertThat(service.getAwsRuleTargetList(ID)).hasSize(0);
    }
}
