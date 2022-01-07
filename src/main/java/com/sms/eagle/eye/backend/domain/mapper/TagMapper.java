package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.TagEntity;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TagMapper extends BaseMapper<TagEntity> {

    List<IdNameResponse<Long>> getList();
}




