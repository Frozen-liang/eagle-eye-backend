package com.sms.eagle.eye.backend.request.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sms.eagle.eye.backend.utils.KeepAsJsonDeserializer;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationChannelRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @NotBlank
    private String name;
    private Integer type;
    @NotNull
    @JsonDeserialize(using = KeepAsJsonDeserializer.class)
    private String config;
}