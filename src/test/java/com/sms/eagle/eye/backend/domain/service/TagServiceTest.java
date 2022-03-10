package com.sms.eagle.eye.backend.domain.service;

import com.sms.eagle.eye.backend.domain.mapper.TagMapper;
import com.sms.eagle.eye.backend.domain.service.impl.TagServiceImpl;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    private final TagService tagService = spy(new TagServiceImpl());
    private final TagMapper tagMapper = mock(TagMapper.class);

    private static final Integer SIZE = 10;
    private static final String NAME = "IdNameResponse";

    @Test
    @DisplayName("Test the getList method in the Tag Service")
    public void getList_test() {
        List<IdNameResponse<Long>> responseList = new ArrayList<>();
        IdNameResponse<Long> idNameResponse = new IdNameResponse<>();
        for (int i = 0; i < SIZE; i++) {
            responseList.add(0,idNameResponse);
        }
        doReturn(tagMapper).when(tagService).getBaseMapper();
        when(tagMapper.getList()).thenReturn(responseList);
        assertThat(tagService.getList()).hasSize(SIZE);
    }

    @Test
    @DisplayName("Test the getList method in the Tag Service")
    public void addTagList_test() {
        List<String> tags = new ArrayList<>();
        doReturn(true).when(tagService).saveBatch(any());
        tagService.addTagList(tags);
        verify(tagService).saveBatch(any());
    }
}
