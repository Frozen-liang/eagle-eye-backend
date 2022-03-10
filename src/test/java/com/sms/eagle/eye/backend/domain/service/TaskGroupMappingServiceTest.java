package com.sms.eagle.eye.backend.domain.service;

import com.sms.eagle.eye.backend.domain.mapper.TaskGroupMappingMapper;
import com.sms.eagle.eye.backend.domain.service.impl.TaskGroupMappingServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

public class TaskGroupMappingServiceTest {

    private final TaskGroupMappingService taskGroupMappingService = spy(new TaskGroupMappingServiceImpl());
    private final TaskGroupMappingMapper taskGroupMappingMapper = mock(TaskGroupMappingMapper.class);

    private static final Integer INTEGER = 1;
    private static final Long ID = 1L;


    @Test
    @DisplayName("Test the getByTaskIdAndAlertLevel method in the Tag TaskGroup Mapping Service")
    public void updateGroupMapping_test() {
        doReturn(true).when(taskGroupMappingService).remove(any());
        List<Long> groupList = new ArrayList<>();
        groupList.add(ID);
        doReturn(true).when(taskGroupMappingService).saveBatch(anyList());
        taskGroupMappingService.updateGroupMapping(ID,groupList);
        verify(taskGroupMappingService).remove(any());
        verify(taskGroupMappingService).saveBatch(anyList());
    }

    @Test
    @DisplayName("Test the getGroupListByTaskId method in the Tag TaskGroup Mapping Service")
    public void getGroupListByTaskId_test() {
        doReturn(taskGroupMappingMapper).when(taskGroupMappingService).getBaseMapper();
        List<Long> list = new ArrayList<>();
        when(taskGroupMappingMapper.getGroupListByTaskId(ID)).thenReturn(list);
        assertThat(taskGroupMappingService.getGroupListByTaskId(ID)).isEqualTo(list);
    }
}
