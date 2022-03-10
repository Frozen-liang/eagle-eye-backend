package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.GROUP_ID_IS_NOT_CORRECT;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskGroupMapper;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class TaskGroupServiceImpl extends ServiceImpl<TaskGroupMapper, TaskGroupEntity>
    implements TaskGroupService {

    public static final Long ROOT_ID = -1L;
    public static final Integer DEFAULT_MAX_INDEX = 0;
    public static final Integer INDEX_STEP = 1;

    @Override
    public List<TaskGroupEntity> getEntityList(List<Long> parentIds) {
        return list(Wrappers.<TaskGroupEntity>lambdaQuery()
            .in(CollectionUtils.isNotEmpty(parentIds), TaskGroupEntity::getParentId, parentIds));
    }

    @Override
    public Integer countByName(String name) {
        return getBaseMapper().selectCountByName(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public void saveFromRequest(TaskGroupRequest request, Integer index) {
        save(TaskGroupEntity.builder()
            .name(request.getName())
            .parentId(request.getParentId())
            .index(index)
            .build());
    }

    @Override
    public void updateFromRequest(TaskGroupRequest request, Integer index) {
        updateById(TaskGroupEntity.builder()
            .id(request.getId())
            .parentId(request.getParentId())
            .index(index)
            .build());
    }

    @Override
    public void rename(Long id, String name) {
        updateById(TaskGroupEntity.builder()
            .id(id)
            .name(name)
            .build());
    }

    @Override
    public Integer getNextIndexByParentId(Long parentId) {
        return getBaseMapper().selectMaxIndexByParentId(parentId).orElse(DEFAULT_MAX_INDEX) + INDEX_STEP;
    }

    @Override
    public void putAllGroupDown(Long parentId, Integer fromIndex, Integer toIndex) {
        getBaseMapper().putAllGroupDown(parentId, fromIndex, toIndex, INDEX_STEP);
    }

    @Override
    public void putAllGroupUp(Long parentId, Integer fromIndex, Integer toIndex) {
        getBaseMapper().putAllGroupUp(parentId, fromIndex, toIndex, INDEX_STEP);
    }

    @Override
    public TaskGroupEntity getEntityById(Long id) {
        return Optional.ofNullable(getById(id)).orElseThrow(() -> new EagleEyeException(GROUP_ID_IS_NOT_CORRECT));
    }

    @Override
    public boolean hasChild(Long id) {
        return count(Wrappers.<TaskGroupEntity>lambdaQuery().eq(TaskGroupEntity::getParentId, id)) > 0;
    }

    @Override
    public void deleteGroup(Long id) {
        removeById(id);
    }

    @Override
    public List<Long> getChildGroupById(Long id) {
        Map<Long, List<TaskGroupEntity>> parentGroupMap = list().stream()
            .collect(Collectors.groupingBy(TaskGroupEntity::getParentId));
        return getChildIdList(id, parentGroupMap);
    }

    private List<Long> getChildIdList(Long id, Map<Long, List<TaskGroupEntity>> map) {
        List<TaskGroupEntity> entities = map.get(id);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities.stream().map(TaskGroupEntity::getId).collect(Collectors.toList());
    }

}




