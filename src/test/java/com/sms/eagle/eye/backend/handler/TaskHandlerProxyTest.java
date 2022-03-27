package com.sms.eagle.eye.backend.handler;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_HANDLER_IS_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.handler.impl.TaskHandlerProxy;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

public class TaskHandlerProxyTest {

    private static final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private static final TaskHandler HANDLER_1 = mock(TaskHandler.class);
    private static final TaskHandler HANDLER_2 = mock(TaskHandler.class);
    private static final TaskHandlerProxy taskHandlerProxy = spy(new TaskHandlerProxy());
    private static final String EXCEPTION_CODE = "code";

    @BeforeAll
    public static void init() {
        // mock TaskHandler
        when(HANDLER_1.ifScheduleBySelf()).thenReturn(Boolean.TRUE);
        when(HANDLER_2.ifScheduleBySelf()).thenReturn(Boolean.FALSE);
        // mock applicationContext.getBeansOfType
        Map<String, TaskHandler> taskHandlerMap = new HashMap<>();
        taskHandlerMap.put("HANDLER_1", HANDLER_1);
        taskHandlerMap.put("HANDLER_2", HANDLER_2);
        when(applicationContext.getBeansOfType(TaskHandler.class)).thenReturn(taskHandlerMap);
        taskHandlerProxy.setApplicationContext(applicationContext);
        taskHandlerProxy.afterPropertiesSet();
    }

    /**
     * {@link TaskHandlerProxy#startTask(TaskOperationRequest)}
     *
     * <p>情形1：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.TRUE 执行 {@link TaskHandlerProxyTest#HANDLER_1} 的 startTask 方法
     */
    @Test
    public void startTask_test_1() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.TRUE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.startTask(request);
        // assert
        verify(HANDLER_1).startTask(request);
    }

    /**
     * {@link TaskHandlerProxy#startTask(TaskOperationRequest)}
     *
     * <p>情形2：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.FALSE 执行 {@link TaskHandlerProxyTest#HANDLER_2} 的 startTask
     * 方法
     */
    @Test
    public void startTask_test_2() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.FALSE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.startTask(request);
        // assert
        verify(HANDLER_2).startTask(request);
    }

    /**
     * {@link TaskHandlerProxy#startTask(TaskOperationRequest)}
     *
     * <p>情形3：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 null 则抛出异常
     */
    @Test
    public void startTask_test_3() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(null);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke and assert
        assertThatThrownBy(() -> taskHandlerProxy.startTask(request)).isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(TASK_HANDLER_IS_NOT_EXIST.getCode());
    }


    /**
     * {@link TaskHandlerProxy#stopTask(TaskOperationRequest)}
     *
     * <p>情形1：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.TRUE 执行 {@link TaskHandlerProxyTest#HANDLER_1} 的 stopTask 方法
     */
    @Test
    public void stopTask_test_1() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.TRUE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.stopTask(request);
        // assert
        verify(HANDLER_1).stopTask(request);
    }

    /**
     * {@link TaskHandlerProxy#stopTask(TaskOperationRequest)}
     *
     * <p>情形2：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.FALSE 执行 {@link TaskHandlerProxyTest#HANDLER_2} 的 stopTask 方法
     */
    @Test
    public void stopTask_test_2() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.FALSE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.stopTask(request);
        // assert
        verify(HANDLER_2).stopTask(request);
    }

    /**
     * {@link TaskHandlerProxy#stopTask(TaskOperationRequest)}
     *
     * <p>情形3：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 null 则抛出异常
     */
    @Test
    public void stopTask_test_3() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(null);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke and assert
        assertThatThrownBy(() -> taskHandlerProxy.stopTask(request)).isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(TASK_HANDLER_IS_NOT_EXIST.getCode());
    }


    /**
     * {@link TaskHandlerProxy#updateTask(TaskOperationRequest)}
     *
     * <p>情形1：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.TRUE 执行 {@link TaskHandlerProxyTest#HANDLER_1} 的 updateTask
     * 方法
     */
    @Test
    public void updateTask_test_1() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.TRUE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.updateTask(request);
        // assert
        verify(HANDLER_1).updateTask(request);
    }

    /**
     * {@link TaskHandlerProxy#updateTask(TaskOperationRequest)}
     *
     * <p>情形2：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 Boolean.FALSE 执行 {@link TaskHandlerProxyTest#HANDLER_2} 的 updateTask
     * 方法
     */
    @Test
    public void updateTask_test_2() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(Boolean.FALSE);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke
        taskHandlerProxy.updateTask(request);
        // assert
        verify(HANDLER_2).updateTask(request);
    }

    /**
     * {@link TaskHandlerProxy#updateTask(TaskOperationRequest)}
     *
     * <p>情形3：{@link TaskOperationRequest} 中的
     * {@link PluginEntity#getScheduleBySelf()} 为 null 则抛出异常
     */
    @Test
    public void updateTask_test_3() {
        // mock PluginEntity
        PluginEntity plugin = mock(PluginEntity.class);
        when(plugin.getScheduleBySelf()).thenReturn(null);
        // mock request
        TaskOperationRequest request = mock(TaskOperationRequest.class);
        when(request.getPlugin()).thenReturn(plugin);
        // invoke and assert
        assertThatThrownBy(() -> taskHandlerProxy.updateTask(request)).isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(TASK_HANDLER_IS_NOT_EXIST.getCode());
    }

    /**
     * {@link TaskHandlerProxy#ifScheduleBySelf()}
     */
    @Test
    public void ifScheduleBySelf_test() {
        assertThat(taskHandlerProxy.ifScheduleBySelf()).isTrue();
    }
}