package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.tag.TagRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.service.TagApplicationService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tag.
 */
@RestController
@RequestMapping("/v1/tag")
public class TagController {

    private final TagApplicationService tagApplicationService;

    public TagController(TagApplicationService tagApplicationService) {
        this.tagApplicationService = tagApplicationService;
    }

    /**
     * 添加Tag.
     */
    @PostMapping
    public Response<List<IdNameResponse<Long>>> addTags(@RequestBody TagRequest request) {
        return Response.ok(tagApplicationService.addTags(request));
    }
}