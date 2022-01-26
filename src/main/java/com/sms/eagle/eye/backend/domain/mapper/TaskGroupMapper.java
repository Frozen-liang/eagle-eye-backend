package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TaskGroupMapper extends BaseMapper<TaskGroupEntity> {

    Integer selectCountByName(@Param("name") String name);

    Optional<Integer> selectMaxIndexByParentId(@Param("parentId") Long parentId);

    Integer putAllGroupDown(@Param("parentId") Long parentId, @Param("fromIndex") Integer fromIndex,
        @Param("toIndex") Integer toIndex, @Param("step") Integer step);

    Integer putAllGroupUp(@Param("parentId") Long parentId, @Param("fromIndex") Integer fromIndex,
        @Param("toIndex") Integer toIndex, @Param("step") Integer step);
}




