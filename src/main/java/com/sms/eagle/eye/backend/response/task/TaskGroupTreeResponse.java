package com.sms.eagle.eye.backend.response.task;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroupTreeResponse extends TaskGroupBasicResponse {

    private List<TaskGroupTreeResponse> child;
}