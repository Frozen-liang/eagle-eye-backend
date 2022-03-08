package com.sms.eagle.eye.backend.request.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(groups = {UpdateGroup.class})
    private Long id;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    private String name;
}
