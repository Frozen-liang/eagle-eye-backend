package com.sms.eagle.eye.backend.listener;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.event.DisablePluginEvent;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PluginEventListenerTest {

    private final TaskService taskService = mock(TaskService.class);
    private final TaskApplicationService taskApplicationService = mock(TaskApplicationService.class);

    private final PluginEventListener pluginEventListener = spy(
        new PluginEventListener(taskService, taskApplicationService));

    /**
     * {@link PluginEventListener#disablePlugin(DisablePluginEvent)}
     */
    @Test
    void disablePlugin_test_1() {
        // mock event
        DisablePluginEvent event = mock(DisablePluginEvent.class);
        // mock
        Long pluginId = 1L;
        when(event.getPluginId()).thenReturn(pluginId);
        // mock taskService.getRunningTaskListByPlugin
        List<Long> runningTaskList = Arrays.asList(1L, 2L);
        when(taskService.getRunningTaskListByPlugin(pluginId)).thenReturn(runningTaskList);
        // mock taskApplicationService.stopByTaskId
        doReturn(true).when(taskApplicationService).stopByTaskId(anyLong());
        // invoke
        pluginEventListener.disablePlugin(event);
        // verify
        verify(taskApplicationService, times(2)).stopByTaskId(anyLong());
    }
}