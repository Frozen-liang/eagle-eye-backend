package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TagEntity;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import java.util.List;

public interface TagService extends IService<TagEntity> {

    List<IdNameResponse<Long>> getList();

    void addTagList(List<String> tags);
}
