package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import org.junit.jupiter.api.Test;

public class TaskConverterTest {

    TaskConverterImpl converter = spy(new TaskConverterImpl());

    @Test
    void toEntity_test1() {
        assertThat(converter.toEntity(null)).isNull();
    }

    @Test
    void toEntity_test2() {
        String name = "name";
        TaskBasicInfoRequest request = mock(TaskBasicInfoRequest.class);
        when(request.getName()).thenReturn(name);
        assertThat(converter.toEntity(request).getName()).isEqualTo(name);
    }
}