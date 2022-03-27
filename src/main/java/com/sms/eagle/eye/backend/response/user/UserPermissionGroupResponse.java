package com.sms.eagle.eye.backend.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionGroupResponse {
    private String email;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long permissionGroupId;
    private String fullName;
    private String workNumber;
}
