package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.service.impl.TaskGroupApplicationServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertTrue;

public class TaskGroupApplicationServiceTest {

    private final TaskGroupService taskGroupService = mock(TaskGroupService.class);
    private final TaskGroupMappingService taskGroupMappingService = mock(TaskGroupMappingService.class);
    private final TaskGroupApplicationService taskGroupApplicationService =
        new TaskGroupApplicationServiceImpl(taskGroupService, taskGroupMappingService);
    private final TaskGroupRequest taskGroupRequest = mock(TaskGroupRequest.class);
    private final TaskGroupEntity taskGroupEntity = mock(TaskGroupEntity.class);

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final Integer INTEGER = 1;

    @Test
    @DisplayName("Test the getGroupListByParentId method in the Tag Application Service")
    public void getGroupListByParentId_test() {
        when(taskGroupService.getEntityList(any())).thenReturn(Collections.emptyList());
        assertThat(taskGroupApplicationService.getGroupListByParentId(ID)).isNotNull();
    }

    private List<TaskGroupResponse> getList(Long id, Map<Long, List<TaskGroupEntity>> map) {
        List<TaskGroupEntity> entities = map.get(id);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
            .sorted(Comparator.comparing(TaskGroupEntity::getIndex))
            .map(entity -> convertToListResponse(entity, map))
            .collect(Collectors.toList());
    }

    private TaskGroupResponse convertToListResponse(TaskGroupEntity entity, Map<Long, List<TaskGroupEntity>> map) {
        return TaskGroupResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .index(entity.getIndex())
            .isLeaf(CollectionUtils.isEmpty(map.get(entity.getId())))
            .build();
    }

    @Test
    @DisplayName("Test the getTaskGroupTreeList method in the Tag Application Service")
    public void getTaskGroupTreeList_test() {
        when(taskGroupService.getEntityList(anyList())).thenReturn(Collections.emptyList());
        assertThat(taskGroupApplicationService.getTaskGroupTreeList()).isNotNull();
    }

    @Test
    @DisplayName("Test the addGroup method in the Tag Application Service")
    public void addGroup_test() {
        when(taskGroupService.countByName(NAME)).thenReturn(INTEGER);
        when(taskGroupService.getNextIndexByParentId(ID)).thenReturn(INTEGER);
        when(taskGroupRequest.getParentId()).thenReturn(ID);
        doNothing().when(taskGroupService).saveFromRequest(taskGroupRequest, INTEGER);
        assertThat(taskGroupApplicationService.addGroup(taskGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the addGroup_exception method in the Tag Application Service")
    public void addGroup_exception_test() {
        when(taskGroupRequest.getName()).thenReturn(NAME);
        when(taskGroupService.countByName(NAME)).thenReturn(INTEGER);
        assertThatThrownBy(() -> taskGroupApplicationService.addGroup(taskGroupRequest))
            .isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the updateGroup method in the Tag Application Service")
    public void updateGroup_test() {
        when(taskGroupRequest.getId()).thenReturn(ID);
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        doNothing().when(taskGroupService).updateFromRequest(taskGroupRequest, INTEGER);
        assertThat(taskGroupApplicationService.updateGroup(taskGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the removeGroup method in the Tag Application Service")
    public void removeGroup_test() {
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        doNothing().when(taskGroupService).deleteGroup(ID);
        when(taskGroupEntity.getParentId()).thenReturn(ID);
        when(taskGroupEntity.getIndex()).thenReturn(INTEGER);
        doNothing().when(taskGroupService).putAllGroupDown(ID, INTEGER, INTEGER);
        assertThat(taskGroupApplicationService.removeGroup(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the removeGroup_exception method in the Tag Application Service")
    public void removeGroup_exception_test() {
        when(taskGroupService.hasChild(ID)).thenReturn(Boolean.TRUE);
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        assertThatThrownBy(() -> taskGroupApplicationService.removeGroup(ID)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the removeGroupByIds method in the Task Group Application Service")
    public void removeGroupByIds_test() {
        List<TaskGroupEntity> entityList = Lists.newArrayList(TaskGroupEntity.builder().id(ID).parentId(ID).build());
        when(taskGroupService.listByIds(any())).thenReturn(entityList);
        when(taskGroupService.getChildGroupByIds(any())).thenReturn(Lists.newArrayList(ID));
        when(taskGroupMappingService.countByGroupIds(any())).thenReturn(0);
        doNothing().when(taskGroupService).deleteGroupByIds(any());
        doNothing().when(taskGroupService).putAllGroupUp(any(), any(), any());
        boolean isSuccess = taskGroupApplicationService.removeGroupByIds(Lists.newArrayList(ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while remove group by ids ")
    public void removeGroupByIds_test_thrownException() {
        List<TaskGroupEntity> entityList = Lists.newArrayList(TaskGroupEntity.builder().id(ID).parentId(ID).build());
        when(taskGroupService.listByIds(any())).thenReturn(entityList);
        when(taskGroupService.getChildGroupByIds(any())).thenReturn(Lists.newArrayList(2L));
        assertThatThrownBy(() -> taskGroupApplicationService.removeGroupByIds(Lists.newArrayList(ID)))
            .isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("An exception occurred while remove group by ids ")
    public void removeGroupByIdsTaskIsNotNull_test_thrownException() {
        List<TaskGroupEntity> entityList = Lists.newArrayList(TaskGroupEntity.builder().id(ID).parentId(ID).build());
        when(taskGroupService.listByIds(any())).thenReturn(entityList);
        when(taskGroupService.getChildGroupByIds(any())).thenReturn(Lists.newArrayList(ID));
        when(taskGroupMappingService.countByGroupIds(any())).thenReturn(INTEGER);
        assertThatThrownBy(() -> taskGroupApplicationService.removeGroupByIds(Lists.newArrayList(ID)))
            .isInstanceOf(EagleEyeException.class);
    }

}
