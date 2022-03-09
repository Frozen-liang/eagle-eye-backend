package com.sms.eagle.eye.backend.request.group;

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
public class TaskGroupRequest {

    @NotNull(groups = {UpdateGroup.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @NotNull(groups = {InsertGroup.class})
    private String name;

    private Integer preIndex;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
}