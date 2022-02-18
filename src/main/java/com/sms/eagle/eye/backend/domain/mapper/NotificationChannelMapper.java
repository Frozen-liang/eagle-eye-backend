package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NotificationChannelMapper extends BaseMapper<NotificationChannelEntity> {

    IPage<NotificationChannelEntity> getPage(Page<?> page, @Param("request") NotificationChannelQueryRequest request);
}
