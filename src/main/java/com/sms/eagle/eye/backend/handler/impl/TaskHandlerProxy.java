package com.sms.eagle.eye.backend.handler.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_HANDLER_IS_NOT_EXIST;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.handler.TaskHandler;
import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Qualifier(TaskHandlerProxy.TASK_HANDLER_PROXY)
public class TaskHandlerProxy implements TaskHandler, ApplicationContextAware, InitializingBean {

    public static final String TASK_HANDLER_PROXY = "taskHandlerProxy";

    private ApplicationContext applicationContext;
    private Map<Boolean, TaskHandler> taskHandlerMap = new HashMap<>();

    private TaskHandler getTaskHandler(TaskOperationRequest request) {
        return Optional.ofNullable(taskHandlerMap.get(request.getPlugin().getScheduleBySelf()))
            .orElseThrow(() -> new EagleEyeException(TASK_HANDLER_IS_NOT_EXIST));
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, TaskHandler> taskHandlerBeanMap = this.applicationContext.getBeansOfType(TaskHandler.class);
        taskHandlerBeanMap.remove(TASK_HANDLER_PROXY);
        taskHandlerMap = taskHandlerBeanMap.values().stream()
            .collect(Collectors.toMap(TaskHandler::ifScheduleBySelf, Function.identity()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void startTask(TaskOperationRequest request) {
        getTaskHandler(request).startTask(request);
    }

    @Override
    public void stopTask(TaskOperationRequest request) {
        getTaskHandler(request).stopTask(request);
    }

    @Override
    public void updateTask(TaskOperationRequest request) {
        getTaskHandler(request).updateTask(request);
    }

    @Override
    public Boolean ifScheduleBySelf() {
        // Fix spotbugsMain: M B NP
        return Boolean.TRUE;
    }
}