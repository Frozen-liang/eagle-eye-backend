package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TaskMapper extends BaseMapper<TaskEntity> {

    IPage<TaskResponse> getPage(Page<?> page, @Param("request") TaskQueryRequest request);

    Optional<Long> getIdByName(@Param("name") String name);

    Integer selectCountByName(@Param("name") String name);

    Optional<Integer> getTaskStatusById(@Param("id") Long id);
}




