package com.sms.eagle.eye.backend.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertNotificationResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long alertNotificationId;
    private Integer channelType;

    private List<ChannelFieldWithValueResponse> input;
}