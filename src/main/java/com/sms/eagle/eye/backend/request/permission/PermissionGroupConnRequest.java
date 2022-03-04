package com.sms.eagle.eye.backend.request.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupConnRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long groupId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long permissionId;
}
