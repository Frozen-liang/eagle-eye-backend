package com.sms.eagle.eye.backend.request.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
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

    @NotNull(groups = UpdateGroup.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    private String name;
    @NotNull(groups = InsertGroup.class)
    private Integer type;
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @JsonDeserialize(using = KeepAsJsonDeserializer.class)
    private String config;
}