package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginAlertRuleMapper;
import com.sms.eagle.eye.backend.domain.service.PluginAlertRuleService;
import com.sms.eagle.eye.backend.request.plugin.AlertRuleRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DomainServiceAdvice
public class PluginAlertRuleServiceImpl extends ServiceImpl<PluginAlertRuleMapper,
    PluginAlertRuleEntity> implements PluginAlertRuleService {

    @Override
    public void updateByRequest(List<AlertRuleRequest> requests, Long pluginId) {
        remove(Wrappers.<PluginAlertRuleEntity>lambdaQuery().eq(PluginAlertRuleEntity::getPluginId, pluginId));
        List<PluginAlertRuleEntity> list = new ArrayList<>();
        requests.forEach(alertRuleRequest -> list.addAll(
            alertRuleRequest.getAlertKeys().stream()
                .map(alertKey -> PluginAlertRuleEntity.builder()
                    .pluginId(pluginId)
                    .alarmLevel(alertRuleRequest.getAlarmLevel())
                    .alertKey(alertKey)
                    .build())
                .collect(Collectors.toList())));
        saveBatch(list);
    }

    @Override
    public List<PluginAlertRuleEntity> getListByPluginId(Long pluginId) {
        return list(Wrappers.<PluginAlertRuleEntity>lambdaQuery()
            .eq(PluginAlertRuleEntity::getPluginId, pluginId));
    }

}