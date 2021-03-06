package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GROUP_NAME_HAS_ALREADY_EXIST;
import static com.sms.eagle.eye.backend.exception.ErrorCode.REMOVE_CHILD_BEFORE_DELETE_GROUP;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.response.task.TaskGroupTreeResponse;
import com.sms.eagle.eye.backend.service.impl.TaskGroupApplicationServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    /**
     * {@link TaskGroupApplicationServiceImpl#getGroupListByParentId(Long)}
     * <p>
     * ???????????? parentId ??????????????????????????????.
     *
     * <p>???parentId?????????ROOT_ID = -1L
     */
    @Test
    @DisplayName("Test the getGroupListByParentId method in the Tag Application Service")
    public void getGroupListByParentId_test() {
        // mock
        TaskGroupEntity entity = mock(TaskGroupEntity.class);
        List<TaskGroupEntity> entityList = mock(List.class);
        when(taskGroupService.getEntityList(any())).thenReturn(entityList);
        // ??????
        List<TaskGroupResponse> result = taskGroupApplicationService.getGroupListByParentId(ID);
        // ??????
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#getTaskGroupTreeList()}
     * <p>
     * ???????????????????????????????????????????????????.
     */
    @Test
    @DisplayName("Test the getTaskGroupTreeList method in the Tag Application Service")
    public void getTaskGroupTreeList_test() {
        // ??????????????????
        Long ID = 1L;
        // mock
        TaskGroupTreeResponse response = mock(TaskGroupTreeResponse.class);
        List<TaskGroupTreeResponse> list = Arrays.asList(response);
        TaskGroupEntity entity = mock(TaskGroupEntity.class);
        List<TaskGroupEntity> entityList = Arrays.asList(entity);
        List<Long> parentIds = Arrays.asList(ID);
        when(taskGroupService.getEntityList(parentIds)).thenReturn(entityList);
        // ??????
        List<TaskGroupTreeResponse> taskGroupTreeList = taskGroupApplicationService.getTaskGroupTreeList();
        // ??????
        assertThat(taskGroupTreeList).isNotNull();
//        assertThat(taskGroupTreeList).isEqualTo(list);
    }

    /**
     * TODO ??????
     * ???????????????.
     * {@link TaskGroupApplicationServiceImpl#addGroup(TaskGroupRequest)}
     * <p>
     * ???????????? TaskGroupRequest????????????????????????????????????????????????.
     *
     * <p>??????1??????????????????
     */
    @Test
    @DisplayName("Test the addGroup method in the Tag Application Service")
    public void addGroup_test() {
        // mock
        when(taskGroupService.countByName(NAME)).thenReturn(INTEGER);
        when(taskGroupService.getNextIndexByParentId(ID)).thenReturn(INTEGER);
        when(taskGroupRequest.getParentId()).thenReturn(ID);
        doNothing().when(taskGroupService).saveFromRequest(taskGroupRequest, INTEGER);
        // ??????
        boolean addGroup = taskGroupApplicationService.addGroup(taskGroupRequest);
        // ??????
        assertThat(addGroup).isTrue();
    }

    /**
     * TODO ??????
     * ???????????????.
     * {@link TaskGroupApplicationServiceImpl#addGroup(TaskGroupRequest)}
     * <p>
     * ???????????? TaskGroupRequest????????????????????????????????????????????????.
     *
     * <p>??????1??????????????????
     */
    @Test
    @DisplayName("Test the addGroup_exception method in the Tag Application Service")
    public void addGroup_exception_test() {
        // mock
        when(taskGroupRequest.getName()).thenReturn(NAME);
        when(taskGroupService.countByName(NAME)).thenReturn(INTEGER);
        // ????????????
        assertThatThrownBy(() -> taskGroupApplicationService.addGroup(taskGroupRequest))
                .isInstanceOf(EagleEyeException.class)
                .extracting("code").isEqualTo(GROUP_NAME_HAS_ALREADY_EXIST.getCode());
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#updateGroup(TaskGroupRequest)}
     * <p>
     * TODO ????????????
     * ???????????? TaskGroupRequest?????????????????????????????????????????????.
     *
     * <p>???????????? ??????????????????????????????
     */
    @Test
    @DisplayName("Test the updateGroup method in the Tag Application Service")
    public void updateGroup_test1() {
        // ??????????????????
        Long ID = 1L;
        Integer INTEGER = 3;
        // mock
        when(taskGroupRequest.getParentId()).thenReturn(ID);
        when(taskGroupRequest.getId()).thenReturn(ID);
        TaskGroupEntity entity = TaskGroupEntity.builder().parentId(ID).index(INTEGER).build();
        when(taskGroupService.getEntityById(ID)).thenReturn(entity);
        doNothing().when(taskGroupService).updateFromRequest(taskGroupRequest, INTEGER);
        // ??????
        boolean updateGroup = taskGroupApplicationService.updateGroup(taskGroupRequest);
        // ??????
        assertThat(updateGroup).isTrue();
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#updateGroup(TaskGroupRequest)}
     * <p>
     * TODO ????????????
     * ???????????? TaskGroupRequest?????????????????????????????????????????????.
     *
     *
     * <p>???????????? ??????????????????????????????
     */
    @Test
    @DisplayName("Test the updateGroup method in the Tag Application Service")
    public void updateGroup_test2() {
        // mock
        when(taskGroupRequest.getId()).thenReturn(ID);
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        doNothing().when(taskGroupService).putAllGroupUp(ID, INTEGER, INTEGER);
        doNothing().when(taskGroupService).updateFromRequest(taskGroupRequest, INTEGER);
        // ??????
        boolean updateGroup = taskGroupApplicationService.updateGroup(taskGroupRequest);
        // ??????
        assertThat(updateGroup).isTrue();
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#updateGroup(TaskGroupRequest)}
     * <p>
     * TODO ????????????
     * ???????????? TaskGroupRequest?????????????????????????????????????????????.
     *
     * <p>???????????? ??????????????????.
     */
    @Test
    @DisplayName("Test the updateGroup method in the Tag Application Service")
    public void updateGroup_test3() {
        // ??????????????????
        Long ID = 0L;
        Integer INTEGER = 0;
        // mock
        when(taskGroupRequest.getId()).thenReturn(ID);
        TaskGroupEntity entity = TaskGroupEntity.builder().index(INTEGER).build();
        when(taskGroupService.getEntityById(ID)).thenReturn(entity);
        doNothing().when(taskGroupService).updateFromRequest(taskGroupRequest, INTEGER);
        // ??????
        boolean updateGroup = taskGroupApplicationService.updateGroup(taskGroupRequest);
        // ??????
        assertThat(updateGroup).isTrue();
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#rename(TaskGroupRequest)}
     * <p>
     * ???????????? TaskGroupRequest????????????????????????.
     */
    @Test
    @DisplayName("Test the removeGroup method in the Tag Application Service")
    public void rename_test() {
        // mock
        TaskGroupRequest request = mock(TaskGroupRequest.class);
        when(request.getId()).thenReturn(ID);
        when(request.getName()).thenReturn(NAME);
        doNothing().when(taskGroupService).rename(ID, NAME);
        // ??????
        boolean rename = taskGroupApplicationService.rename(request);
        // ??????
        assertThat(rename).isTrue();
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#rename(TaskGroupRequest)}
     *
     * <p>
     * ????????? ????????????
     */
    @Test
    @DisplayName("Test the removeGroup method in the Tag Application Service")
    public void rename_exception_test() {
        // mock
        when(taskGroupRequest.getName()).thenReturn(NAME);
        when(taskGroupService.countByName(NAME)).thenReturn(INTEGER);
        // ????????????
        assertThatThrownBy(() -> taskGroupApplicationService.rename(taskGroupRequest))
                .isInstanceOf(EagleEyeException.class)
                .extracting("code").isEqualTo(GROUP_NAME_HAS_ALREADY_EXIST.getCode());
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#removeGroup(Long)}
     * <p>
     * ???????????? id???????????????.
     * <p>
     * ??????1?????????????????? ?????????????????????
     */
    @Test
    @DisplayName("Test the removeGroup method in the Tag Application Service")
    public void removeGroup_test() {
        // mock
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        doNothing().when(taskGroupService).deleteGroup(ID);
        when(taskGroupEntity.getParentId()).thenReturn(ID);
        when(taskGroupEntity.getIndex()).thenReturn(INTEGER);
        doNothing().when(taskGroupService).putAllGroupDown(ID, INTEGER, INTEGER);
        assertThat(taskGroupApplicationService.removeGroup(ID)).isTrue();
    }

    /**
     * {@link TaskGroupApplicationServiceImpl#removeGroup(Long)}
     * <p>
     * ???????????? id???????????????.
     * <p>
     * ??????2???????????????????????????,??????????????????.
     */
    @Test
    @DisplayName("Test the removeGroup_exception method in the Tag Application Service")
    public void removeGroup_exception_test() {
        // mock
        when(taskGroupService.hasChild(ID)).thenReturn(Boolean.TRUE);
        when(taskGroupService.getEntityById(ID)).thenReturn(taskGroupEntity);
        assertThatThrownBy(() -> taskGroupApplicationService.removeGroup(ID)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the removeGroupByIds method in the Task Group Application Service")
    public void removeGroupByIds_test() {
        TaskGroupEntity entity = TaskGroupEntity.builder().id(ID).parentId(ID).build();
        List<TaskGroupEntity> entityList = Lists.newArrayList(entity);
        when(taskGroupService.listByIds(any())).thenReturn(entityList);
        when(taskGroupService.getChildGroupByIds(any())).thenReturn(Lists.newArrayList(ID));
        when(taskGroupMappingService.countByGroupIds(any())).thenReturn(0);
        when(taskGroupService.getEntityById(any())).thenReturn(entity);
        doNothing().when(taskGroupService).deleteGroup(any());
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
