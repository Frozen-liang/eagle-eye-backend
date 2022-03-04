package com.sms.eagle.eye.backend.response.permission;

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
public class PermissionGroupResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String creator;
    private List<PermissionResponse> permissions;
}
