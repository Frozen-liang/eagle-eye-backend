package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.ThirdPartyMappingEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ThirdPartyMappingMapper extends BaseMapper<ThirdPartyMappingEntity> {

    Optional<Long> getTaskIdByMappingId(@Param("mappingId") String mappingId, @Param("type") Integer type);

    Optional<String> getMappingIdByTaskId(@Param("taskId") Long taskId, @Param("type") Integer type);
}




