package com.sms.eagle.eye.backend.request.password;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

    @NotNull(groups = {UpdateGroup.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    private String name;
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    private String password;
    private String description;
}