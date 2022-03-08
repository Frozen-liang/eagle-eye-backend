package com.sms.eagle.eye.backend.response.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDetailResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private List<ChannelFieldWithValueResponse> config;
}