package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.request.tag.TagRequest;
import com.sms.eagle.eye.backend.service.impl.TagApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TagApplicationServiceTest {

    private final TagService tagService = mock(TagService.class);
    private final TagApplicationService tagApplicationService = new TagApplicationServiceImpl(tagService);
    private final TagRequest tagRequest = mock(TagRequest.class);

    @Test
    @DisplayName("Test the addTags method in the Tag Application Service")
    public void addTags() {
        doNothing().when(tagService).addTagList(anyList());
        when(tagService.getList()).thenReturn(Collections.emptyList());
        assertThat(tagApplicationService.addTags(tagRequest)).isNotNull();
    }
}
