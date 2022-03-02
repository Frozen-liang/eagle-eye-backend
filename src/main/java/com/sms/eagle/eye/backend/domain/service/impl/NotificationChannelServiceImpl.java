package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_ID_IS_NOT_CORRECT;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.mapper.NotificationChannelMapper;
import com.sms.eagle.eye.backend.domain.service.NotificationChannelService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class NotificationChannelServiceImpl extends ServiceImpl<NotificationChannelMapper, NotificationChannelEntity>
    implements NotificationChannelService {

    @Override
    public List<ChannelListResponse> getList() {
        return list().stream().map(entity -> {
            ChannelListResponse response = ChannelListResponse.builder().build();
            BeanUtils.copyProperties(entity, response);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<NotificationChannelEntity> getPage(NotificationChannelQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Override
    public NotificationChannelEntity getEntityById(Long channelId) {
        return Optional.ofNullable(getById(channelId))
            .orElseThrow(() -> new EagleEyeException(CHANNEL_ID_IS_NOT_CORRECT));
    }

    @Override
    public void saveFromRequest(NotificationChannelRequest request) {
        NotificationChannelEntity entity = NotificationChannelEntity.builder().build();
        BeanUtils.copyProperties(request, entity);
        entity.setCreator(SecurityUtil.getCurrentUser().getUsername());
        save(entity);
    }

    @Override
    public void updateFromRequest(NotificationChannelRequest request) {
        NotificationChannelEntity entity = NotificationChannelEntity.builder().build();
        BeanUtils.copyProperties(request, entity);
        updateById(entity);
    }

    @Override
    public void deleteEntity(Long id) {
        removeById(id);
    }

}




