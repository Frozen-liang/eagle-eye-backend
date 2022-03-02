package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PluginAlertFieldMapper extends BaseMapper<PluginAlertFieldEntity> {

}