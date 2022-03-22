package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupMappingEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TaskGroupMappingMapper extends BaseMapper<TaskGroupMappingEntity> {

    List<Long> getGroupListByTaskId(@Param("taskId") Long taskId);

    Integer countByGroupId(@Param("groupId") Long groupId);

    Integer countByGroupIds(@Param("groupIds") List<Long> groupIds);
}




