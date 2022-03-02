package com.sms.eagle.eye.backend.common.enums;

import com.sms.eagle.eye.backend.service.DataApplicationService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

@Getter
public enum SelectData {
    PLUGIN("plugin", SelectData::getPluginList),
    SCHEDULE_UNIT("scheduleUnit", SelectData::getScheduleUnit),
    TASK_TAG("tag", SelectData::getTagList),
    TASK_STATUS("taskStatus", SelectData::getTaskStatusList),
    ALARM_LEVEL("alarmLevel", SelectData::getAlarmLevel),
    TEMPLATE_TYPE("templateType", SelectData::getTemplateType);

    private final String key;
    private final Function<DataApplicationService, Object> function;

    SelectData(String key,
        Function<DataApplicationService, Object> function) {
        this.key = key;
        this.function = function;
    }

    private static Object getPluginList(DataApplicationService dataApplicationService) {
        return dataApplicationService.getPluginList();
    }

    private static Object getScheduleUnit(DataApplicationService dataApplicationService) {
        return dataApplicationService.getScheduleUnitList();
    }

    private static Object getTagList(DataApplicationService dataApplicationService) {
        return dataApplicationService.getTagList();
    }

    private static Object getTaskStatusList(DataApplicationService dataApplicationService) {
        return dataApplicationService.getTaskStatusList();
    }

    private static Object getAlarmLevel(DataApplicationService dataApplicationService) {
        return dataApplicationService.getAlarmLevelResponse();
    }

    private static Object getTemplateType(DataApplicationService dataApplicationService) {
        return dataApplicationService.getTemplateType();
    }

    public static Map<String, Object> getSelectData(DataApplicationService dataApplicationService) {
        Map<String, Object> map = new HashMap<>();
        for (SelectData selectData : values()) {
            map.put(selectData.getKey(), selectData.getFunction().apply(dataApplicationService));
        }
        return map;
    }
}
