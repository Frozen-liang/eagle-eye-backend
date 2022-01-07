package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.tag.TagRequest;
import com.sms.eagle.eye.backend.service.TagApplicationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagApplicationServiceImpl implements TagApplicationService {

    private final TagService tagService;

    public TagApplicationServiceImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public List<IdNameResponse<Long>> addTags(TagRequest request) {
        tagService.addTagList(request.getTags());
        return tagService.getList();
    }
}