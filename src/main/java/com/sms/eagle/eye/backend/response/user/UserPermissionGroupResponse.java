package com.sms.eagle.eye.backend.response.user;

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
    private String permissionGroupName;
}
