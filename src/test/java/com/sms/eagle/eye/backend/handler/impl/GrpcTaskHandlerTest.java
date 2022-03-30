package com.sms.eagle.eye.backend.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.factory.PluginClient;
import com.sms.eagle.eye.backend.factory.PluginClientFactory;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.model.TaskAlertRule;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import com.sms.eagle.eye.backend.utils.TaskScheduleUtil;
import com.sms.eagle.eye.plugin.v1.CreateTaskRequest;
import com.sms.eagle.eye.plugin.v1.CreateTaskResponse;
import com.sms.eagle.eye.plugin.v1.DeleteTaskRequest;
import com.sms.eagle.eye.plugin.v1.GeneralResponse;
import com.sms.eagle.eye.plugin.v1.PluginServiceGrpc;
import com.sms.eagle.eye.plugin.v1.UpdateTaskRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class GrpcTaskHandlerTest {

    private final PluginClientFactory factory = mock(PluginClientFactory.class);
    private final TaskService taskService = mock(TaskService.class);
    private final ThirdPartyMappingService thirdPartyMappingService = mock(ThirdPartyMappingService.class);
    private final InvokeErrorRecordService invokeErrorRecordService = mock(InvokeErrorRecordService.class);

    private final TaskHandler taskHandler = spy(new GrpcTaskHandler(
        factory, taskService, thirdPartyMappingService, invokeErrorRecordService));

    private static final MockedStatic<TaskScheduleUtil> TASK_SCHEDULE_UTIL_MOCKED_STATIC
        = mockStatic(TaskScheduleUtil.class);

    @AfterAll
    public static void close() {
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.close();
    }

    /**
     * {@link GrpcTaskHandler#startTask(TaskOperationRequest)}
     *
     * <p>情形1：Grpc调用成功并返回一个与任务id对应的第三方系统的id
     */
    @Test
    public void startTask_test_1() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getAlertRules()
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        List<TaskAlertRule> alertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(alertRules);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        //mock taskAlertRule.getDecryptedAlertRule()
        String rule = "rule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(rule);
        // static mock
        Integer interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String name = "name";
        when(taskEntity.getName()).thenReturn(name);
        // mock taskEntity.getDescription()
        String description = "description";
        when(taskEntity.getDescription()).thenReturn(description);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock stub.createOrExecute()
        CreateTaskResponse response = mock(CreateTaskResponse.class);
        when(stub.createOrExecute(any(CreateTaskRequest.class))).thenReturn(response);
        // mock response.getId()
        String thirdPartyId = "123";
        when(response.getId()).thenReturn(thirdPartyId);
        // invoke
        taskHandler.startTask(operationRequest);
        // verify
        verify(thirdPartyMappingService).addPluginSystemUnionIdMapping(taskId, thirdPartyId);
    }

    /**
     * {@link GrpcTaskHandler#startTask(TaskOperationRequest)}
     *
     * <p>情形2：Grpc调用成功并返回直接返回任务id
     */
    @Test
    public void startTask_test_2() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getAlertRules()
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        List<TaskAlertRule> alertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(alertRules);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        //mock taskAlertRule.getDecryptedAlertRule()
        String rule = "rule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(rule);
        // static mock
        Integer interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String name = "name";
        when(taskEntity.getName()).thenReturn(name);
        // mock taskEntity.getDescription()
        String description = "description";
        when(taskEntity.getDescription()).thenReturn(description);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock stub.createOrExecute()
        CreateTaskResponse response = mock(CreateTaskResponse.class);
        when(stub.createOrExecute(any(CreateTaskRequest.class))).thenReturn(response);
        // mock response.getId()
        when(response.getId()).thenReturn(taskId.toString());
        // invoke
        taskHandler.startTask(operationRequest);
        // verify
        verify(thirdPartyMappingService, times(0))
            .addPluginSystemUnionIdMapping(any(), any());
    }

    /**
     * {@link GrpcTaskHandler#startTask(TaskOperationRequest)}
     *
     * <p>情形3：Grpc调用失败
     */
    @Test
    public void startTask_test_3() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getAlertRules()
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        List<TaskAlertRule> alertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(alertRules);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        //mock taskAlertRule.getDecryptedAlertRule()
        String rule = "rule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(rule);
        // static mock
        Integer interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String name = "name";
        when(taskEntity.getName()).thenReturn(name);
        // mock taskEntity.getDescription()
        String description = "description";
        when(taskEntity.getDescription()).thenReturn(description);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock exception stub.createOrExecute()
        RuntimeException exception = new RuntimeException();
        when(stub.createOrExecute(any(CreateTaskRequest.class))).thenThrow(exception);
        // invoke
        assertThatThrownBy(() -> taskHandler.startTask(operationRequest))
            .isInstanceOf(Exception.class);
        // verify
        verify(thirdPartyMappingService, times(0)).addPluginSystemUnionIdMapping(any(), any());
        verify(invokeErrorRecordService).addErrorRecord(taskId, exception.getMessage());
        verify(taskService).updateTaskEntity(any());
    }

    /**
     * {@link GrpcTaskHandler#stopTask(TaskOperationRequest)}
     *
     * <p>情形1：根据任务id找到第三方id，
     * 将第三方id作为mappingId完成grpc调用
     */
    @Test
    void stopTask_test_1() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        String mappingId = "mappingId";
        Optional<String> mappingIdOptional = Optional.of(mappingId);
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(mappingIdOptional);
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // build DeleteTaskRequest
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest.newBuilder()
            .setMappingId(mappingId)
            .setConfig(decryptedConfig)
            .build();
        // mock stub.remove()
        GeneralResponse response = mock(GeneralResponse.class);
        when(stub.remove(deleteTaskRequest)).thenReturn(response);
        // invoke
        taskHandler.stopTask(operationRequest);
        // verify
        verify(factory.getClient(url).getBlockingStub()).remove(deleteTaskRequest);
    }

    /**
     * {@link GrpcTaskHandler#stopTask(TaskOperationRequest)}
     *
     * <p>情形2：根据任务id没找到第三方id，
     * 将任务id作为mappingId完成grpc调用
     */
    @Test
    void stopTask_test_2() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(Optional.empty());
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // build DeleteTaskRequest
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest.newBuilder()
            .setMappingId(taskId.toString())
            .setConfig(decryptedConfig)
            .build();
        // mock stub.remove()
        GeneralResponse response = mock(GeneralResponse.class);
        when(stub.remove(deleteTaskRequest)).thenReturn(response);
        // invoke
        taskHandler.stopTask(operationRequest);
        // verify
        verify(factory.getClient(url).getBlockingStub()).remove(deleteTaskRequest);
    }

    /**
     * {@link GrpcTaskHandler#stopTask(TaskOperationRequest)}
     *
     * <p>情形3：在grpc调用时报错
     */
    @Test
    void stopTask_test_3() {
        // mock request
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(Optional.empty());
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // build DeleteTaskRequest
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest.newBuilder()
            .setMappingId(taskId.toString())
            .setConfig(decryptedConfig)
            .build();
        // mock exception stub.remove()
        RuntimeException exception = new RuntimeException();
        when(stub.remove(deleteTaskRequest)).thenThrow(exception);
        // invoke and verify
        assertThatThrownBy(() -> taskHandler.stopTask(operationRequest))
            .isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link GrpcTaskHandler#updateTask(TaskOperationRequest)}
     *
     * <p>情形1：根据任务id找到第三方id，完成grpc调用
     */
    @Test
    void updateTask_test_1() {
        // mock operationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String taskName = "taskName";
        when(taskEntity.getName()).thenReturn(taskName);
        // mock taskEntity.getDescription()
        String taskDescription = "taskDescription";
        when(taskEntity.getDescription()).thenReturn(taskDescription);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskAlertRule.getDecryptedAlertRule()
        String decryptedAlertRule = "decryptedAlertRule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(decryptedAlertRule);
        // static mock
        int interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        String mappingId = "mappingId";
        Optional<String> mappingIdOptional = Optional.of(mappingId);
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(mappingIdOptional);
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock stub
        GeneralResponse response = mock(GeneralResponse.class);
        when(stub.edit(any(UpdateTaskRequest.class))).thenReturn(response);
        // invoke
        taskHandler.updateTask(operationRequest);
        // verify
        verify(factory.getClient(url).getBlockingStub()).edit(any(UpdateTaskRequest.class));
    }

    /**
     * {@link GrpcTaskHandler#updateTask(TaskOperationRequest)}
     *
     * <p>情形2：根据任务id没找到第三方id，完成grpc调用
     */
    @Test
    void updateTask_test_2() {
        // mock operationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String taskName = "taskName";
        when(taskEntity.getName()).thenReturn(taskName);
        // mock taskEntity.getDescription()
        String taskDescription = "taskDescription";
        when(taskEntity.getDescription()).thenReturn(taskDescription);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskAlertRule.getDecryptedAlertRule()
        String decryptedAlertRule = "decryptedAlertRule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(decryptedAlertRule);
        // static mock
        int interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(Optional.empty());
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock stub
        GeneralResponse response = mock(GeneralResponse.class);
        when(stub.edit(any(UpdateTaskRequest.class))).thenReturn(response);
        // invoke
        taskHandler.updateTask(operationRequest);
        // verify
        verify(factory.getClient(url).getBlockingStub()).edit(any(UpdateTaskRequest.class));
    }

    /**
     * {@link GrpcTaskHandler#updateTask(TaskOperationRequest)}
     *
     * <p>情形3：在grpc调用时报错
     */
    @Test
    void updateTask_test_3() {
        // mock operationRequest
        TaskOperationRequest operationRequest = mock(TaskOperationRequest.class);
        // mock operationRequest.getTask()
        TaskEntity taskEntity = mock(TaskEntity.class);
        when(operationRequest.getTask()).thenReturn(taskEntity);
        // mock taskEntity.getId()
        Long taskId = 1L;
        when(taskEntity.getId()).thenReturn(taskId);
        // mock taskEntity.getName()
        String taskName = "taskName";
        when(taskEntity.getName()).thenReturn(taskName);
        // mock taskEntity.getDescription()
        String taskDescription = "taskDescription";
        when(taskEntity.getDescription()).thenReturn(taskDescription);
        // mock operationRequest.getDecryptedConfig()
        String decryptedConfig = "decryptedConfig";
        when(operationRequest.getDecryptedConfig()).thenReturn(decryptedConfig);
        // mock operationRequest.getPlugin()
        PluginEntity plugin = mock(PluginEntity.class);
        when(operationRequest.getPlugin()).thenReturn(plugin);
        // mock plugin.getUrl()
        String url = "url";
        when(plugin.getUrl()).thenReturn(url);
        // mock TaskAlertRule
        TaskAlertRule taskAlertRule = mock(TaskAlertRule.class);
        // mock taskAlertRule.getAlarmLevel()
        String alarmLevel = "alarmLevel";
        when(taskAlertRule.getAlarmLevel()).thenReturn(alarmLevel);
        // mock taskAlertRule.getDecryptedAlertRule()
        String decryptedAlertRule = "decryptedAlertRule";
        when(taskAlertRule.getDecryptedAlertRule()).thenReturn(decryptedAlertRule);
        // static mock
        int interval = 10;
        TASK_SCHEDULE_UTIL_MOCKED_STATIC.when(() -> TaskScheduleUtil.getMinuteInterval(taskAlertRule))
            .thenReturn(interval);
        // mock operationRequest.getAlertRules()
        List<TaskAlertRule> taskAlertRules = new ArrayList<>(Collections.singletonList(taskAlertRule));
        when(operationRequest.getAlertRules()).thenReturn(taskAlertRules);
        // mock thirdPartyMappingService.getPluginSystemUnionId
        when(thirdPartyMappingService.getPluginSystemUnionId(taskId)).thenReturn(Optional.empty());
        // mock factory.getClient()
        PluginClient pluginClient = mock(PluginClient.class);
        when(factory.getClient(url)).thenReturn(pluginClient);
        // mock pluginClient.getBlockingStub()
        PluginServiceGrpc.PluginServiceBlockingStub stub = mock(PluginServiceGrpc.PluginServiceBlockingStub.class);
        when(pluginClient.getBlockingStub()).thenReturn(stub);
        // mock exception stub.remove()
        RuntimeException exception = new RuntimeException();
        when(stub.edit(any(UpdateTaskRequest.class))).thenThrow(exception);
        // invoke and verify
        assertThatThrownBy(() -> taskHandler.updateTask(operationRequest))
            .isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link GrpcTaskHandler#ifScheduleBySelf()}
     */
    @Test
    public void ifScheduleBySelf_test() {
        assertThat(taskHandler.ifScheduleBySelf()).isTrue();
    }


}