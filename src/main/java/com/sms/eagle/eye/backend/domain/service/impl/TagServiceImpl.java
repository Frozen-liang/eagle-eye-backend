package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TagEntity;
import com.sms.eagle.eye.backend.domain.mapper.TagMapper;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity>
    implements TagService {

    @Override
    public List<IdNameResponse<Long>> getList() {
        return getBaseMapper().getList();
    }

    @Override
    public void addTagList(List<String> tags) {
        saveBatch(tags.stream()
            .map(item -> TagEntity.builder()
                .name(item)
                .build())
            .collect(Collectors.toList()));
    }
}




