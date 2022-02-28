package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PluginAlertRuleMapper extends BaseMapper<PluginAlertRuleEntity> {

}