package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.tag.TagRequest;
import java.util.List;

public interface TagApplicationService {

    /**
     * 添加tag，并返回所有tag列表.
     */
    List<IdNameResponse<Long>> addTags(TagRequest request);
}
