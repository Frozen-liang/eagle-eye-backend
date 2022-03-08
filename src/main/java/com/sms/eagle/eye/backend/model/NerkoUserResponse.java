package com.sms.eagle.eye.backend.model;

import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NerkoUserResponse {

    private String code;
    private String message;
    @Default
    private List<UserPermissionGroupResponse> data = new ArrayList<>();
}
