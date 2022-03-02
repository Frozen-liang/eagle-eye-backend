package com.sms.eagle.eye.backend.response.template;

import com.sms.eagle.eye.backend.model.TemplateField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateDetailResponse {

    private String template;
    private List<TemplateField> fieldList;
}