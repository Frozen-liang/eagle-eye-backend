package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.tag.TagRequest;
import com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl;
import com.sms.eagle.eye.backend.service.impl.TagApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TagApplicationServiceTest {

    private final TagService tagService = mock(TagService.class);
    private final TagApplicationService tagApplicationService = new TagApplicationServiceImpl(tagService);
    private final TagRequest tagRequest = mock(TagRequest.class);

    /**
     * {@link TagApplicationServiceImpl#addTags(TagRequest)}.
     *
     * <p>根据 TagRequest参数 添加标签
     */
    @Test
    @DisplayName("Test the addTags method in the Tag Application Service")
    public void addTags() {
        // mock
        List<IdNameResponse<Long>> list = mock(List.class);
        List<String> stringList = mock(List.class);
        doNothing().when(tagService).addTagList(stringList);
        when(tagService.getList()).thenReturn(list);
        // 执行
        List<IdNameResponse<Long>> result = tagApplicationService.addTags(tagRequest);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(list);
    }
}
