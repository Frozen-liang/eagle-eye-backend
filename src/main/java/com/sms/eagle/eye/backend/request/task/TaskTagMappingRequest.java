package com.sms.eagle.eye.backend.request.task;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskTagMappingRequest {

    private Long taskId;
    private List<Long> tagList;
}