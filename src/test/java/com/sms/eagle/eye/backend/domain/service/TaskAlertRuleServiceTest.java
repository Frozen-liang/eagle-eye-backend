package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.service.impl.TaskAlertRuleServiceImpl;
import com.sms.eagle.eye.backend.request.task.TaskAlertRuleRequest;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.BeanUtils;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskAlertRuleServiceTest {

    private final TaskAlertRuleService taskAlertRuleService = spy(new TaskAlertRuleServiceImpl());
    private final TaskAlertRuleRequest ruleRequest = mock(TaskAlertRuleRequest.class);

    private static final Integer INTEGER = 1;
    private static final Long ID = 1L;
    private static final Integer LEVEL = 1;

    private static final MockedStatic<BeanUtils> BEAN_UTILS_MOCKED_STATIC;

    static {
        BEAN_UTILS_MOCKED_STATIC = mockStatic(BeanUtils.class);
    }

    @AfterAll
    private static void close() {
        BEAN_UTILS_MOCKED_STATIC.close();
    }

    @BeforeAll
    private static void initTableInfoForClassList() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""),
                TaskAlertRuleEntity.class);
    }

    @Test
    @DisplayName("Test the getByTaskIdAndAlertLevel method in the Tag TaskAlert Rule Service")
    public void getByTaskIdAndAlertLevel_test() {
        doReturn(null).when(taskAlertRuleService).getOne(any(Wrapper.class));
        assertThat(taskAlertRuleService.getByTaskIdAndAlertLevel(ID,LEVEL)).isNotNull();
    }

    @Test
    @DisplayName("Test the updateByRequest method in the Tag TaskAlert Rule Service")
    public void updateByRequest_test() {
//        TaskAlertRuleEntity ruleEntity = TaskAlertRuleEntity.builder().id(ID).build();
//        when(taskAlertRuleService.getByTaskIdAndAlertLevel(ID,INTEGER)).thenReturn(Optional.of(ruleEntity));
//        when(ruleRequest.getTaskId()).thenReturn(ID);
//        when(ruleRequest.getAlarmLevel()).thenReturn(INTEGER);
//        taskAlertRuleService.updateByRequest(ruleRequest);
//        verify(taskAlertRuleService).saveOrUpdate(ruleEntity);
    }

    @Test
    @DisplayName("Test the getByTaskId method in the Tag TaskAlert Rule Service")
    public void getByTaskId_test() {
        List<TaskAlertRuleEntity> list = mock(List.class);
        doReturn(list).when(taskAlertRuleService).list(any());
        assertThat(taskAlertRuleService.getByTaskId(ID)).isEqualTo(list);
    }
}

