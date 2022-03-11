package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskGroupMapper;
import com.sms.eagle.eye.backend.domain.service.impl.TaskGroupServiceImpl;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskGroupServiceTest {

    private final TaskGroupService taskGroupService = spy(new TaskGroupServiceImpl());
    private final TaskGroupMapper taskGroupMapper = mock(TaskGroupMapper.class);
    private final TaskGroupRequest groupRequest = mock(TaskGroupRequest.class);
    private final TaskGroupEntity entity = mock(TaskGroupEntity.class);

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final Integer INTEGER = 1;

    @Test
    @DisplayName("Test the getEntityList method in the Task Task Group Service")
    public void getEntityList_test() {
        List<TaskGroupEntity> list = mock(List.class);
        doReturn(list).when(taskGroupService).list(any());
        List<Long> parentIds = new ArrayList<>();
        parentIds.add(ID);
        assertThat(taskGroupService.getEntityList(parentIds)).hasSize(0);
    }

    @Test
    @DisplayName("Test the getEntityList method in the Task Task Group Service")
    public void countByName_test() {
        doReturn(taskGroupMapper).when(taskGroupService).getBaseMapper();
        when(taskGroupMapper.selectCountByName(NAME)).thenReturn(INTEGER);
        assertThat(taskGroupService.countByName(NAME)).isEqualTo(0);
    }

    @Test
    @DisplayName("Test the saveFromRequest method in the Task Task Group Service")
    public void saveFromRequest_test() {
        when(groupRequest.getParentId()).thenReturn(ID);
        doReturn(Boolean.TRUE).when(taskGroupService).save(any(TaskGroupEntity.class));
        taskGroupService.saveFromRequest(groupRequest,INTEGER);
        verify(taskGroupService).save(any());
    }

    @Test
    @DisplayName("Test the updateFromRequest method in the Task Task Group Service")
    public void updateFromRequest_test() {
        when(groupRequest.getName()).thenReturn(NAME);
        doReturn(true).when(taskGroupService).updateById(any(TaskGroupEntity.class));
        taskGroupService.updateFromRequest(groupRequest,0);
        verify(taskGroupService).updateById(any());
    }

    @Test
    @DisplayName("Test the getNextIndexByParentId method in the Task Task Group Service")
    public void getNextIndexByParentId_test() {
        doReturn(taskGroupMapper).when(taskGroupService).getBaseMapper();
        when(taskGroupMapper.selectMaxIndexByParentId(ID)).thenReturn(Optional.empty());
        assertThat(taskGroupService.getNextIndexByParentId(ID)).isEqualTo(INTEGER);
    }

    @Test
    @DisplayName("Test the putAllGroupDown method in the Task Task Group Service")
    public void putAllGroupDown_test() {
        doReturn(taskGroupMapper).when(taskGroupService).getBaseMapper();
        when(taskGroupMapper.putAllGroupUp(ID,INTEGER,INTEGER,INTEGER)).thenReturn(INTEGER);
        taskGroupService.putAllGroupUp(ID,INTEGER,INTEGER);
        verify(taskGroupService).getBaseMapper();
    }

    @Test
    @DisplayName("Test the putAllGroupUp method in the Task Task Group Service")
    public void putAllGroupUp_test() {
        doReturn(taskGroupMapper).when(taskGroupService).getBaseMapper();
        when(taskGroupMapper.putAllGroupDown(ID,INTEGER,INTEGER,INTEGER)).thenReturn(INTEGER);
        taskGroupService.putAllGroupDown(ID,INTEGER,INTEGER);
        verify(taskGroupService).getBaseMapper();
    }

    @Test
    @DisplayName("Test the getEntityById method in the Task Task Group Service")
    public void getEntityById_test() {
//        doReturn(null).when(taskGroupService).getById(anyString());
//        assertThat(taskGroupService.getEntityById(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test the hasChild method in the Task Task Group Service")
    public void hasChild_test() {
        when(entity.getParentId()).thenReturn(ID);
        doReturn(ID).when(taskGroupService).count(any(Wrapper.class));
        assertThat(taskGroupService.hasChild(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteGroup method in the Task Task Group Service")
    public void deleteGroup_test() {
        doReturn(Boolean.TRUE).when(taskGroupService).removeById(any(Serializable.class));
        taskGroupService.deleteGroup(ID);
        verify(taskGroupService).removeById(ID);
    }

    @Test
    @DisplayName("Test the getChildGroupById method in the Task Task Group Service")
    public void getChildGroupById_test() {
//        List<Long> list = mock(List.class);
//        Map<Long, List<TaskGroupEntity>> parentGroupMap = new HashMap<>();
//        doReturn(list).when(taskGroupService).list(any(Wrapper.class));
//        doReturn(true).when(getChildIdList(any(),any()));
//        assertThat(taskGroupService.getChildGroupById(ID)).isEqualTo(list);
    }

    private List<Long> getChildIdList(Long id, Map<Long, List<TaskGroupEntity>> map) {
        List<TaskGroupEntity> entities = map.get(id);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities.stream().map(TaskGroupEntity::getId).collect(Collectors.toList());
    }
}
