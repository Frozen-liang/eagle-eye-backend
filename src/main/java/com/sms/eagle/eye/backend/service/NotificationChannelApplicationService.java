package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import java.util.List;

public interface NotificationChannelApplicationService {

    List<ChannelListResponse> getList();

    CustomPage<ChannelPageResponse> getPage(NotificationChannelQueryRequest request);

    List<ChannelFieldResponse> getConfigFieldsByType(Integer type);

    List<ChannelFieldResponse> getInputFieldsByType(Integer type);

    ChannelDetailResponse getByChannelId(Long channelId);

    boolean addChannel(NotificationChannelRequest request);

    boolean updateChannel(NotificationChannelRequest request);

    boolean removeChannel(Long channelId);
}
