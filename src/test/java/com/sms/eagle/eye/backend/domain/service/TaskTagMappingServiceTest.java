package com.sms.eagle.eye.backend.domain.service;

import com.sms.eagle.eye.backend.domain.mapper.TaskTagMappingMapper;
import com.sms.eagle.eye.backend.domain.service.impl.TaskTagMappingServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskTagMappingServiceTest {

    private final TaskTagMappingService taskTagMappingService = spy(new TaskTagMappingServiceImpl());
    private final TaskTagMappingMapper taskGroupMappingMapper = mock(TaskTagMappingMapper.class);

    private static final Integer INTEGER = 1;
    private static final Long ID = 1L;

    @Test
    @DisplayName("Test the getByTaskIdAndAlertLevel method in the Tag TaskGroup Mapping Service")
    public void updateGroupMapping_test() {
        List<Long> tagList = mock(List.class);
        tagList.add(ID);
        doReturn(true).when(taskTagMappingService).remove(any());
        doReturn(true).when(taskTagMappingService).saveBatch(any());
        taskTagMappingService.updateTagMapping(ID,tagList);
        verify(taskTagMappingService).remove(any());
        verify(taskTagMappingService).saveBatch(any());
    }

    @Test
    @DisplayName("Test the getGroupListByTaskId method in the Tag TaskGroup Mapping Service")
    public void getGroupListByTaskId_test() {
        doReturn(taskGroupMappingMapper).when(taskTagMappingService).getBaseMapper();
        List<Long> list = new ArrayList<>();
        when(taskGroupMappingMapper.getTagListByTaskId(ID)).thenReturn(list);
        assertThat(taskTagMappingService.getTagListByTaskId(ID)).isEqualTo(list);
    }
}
