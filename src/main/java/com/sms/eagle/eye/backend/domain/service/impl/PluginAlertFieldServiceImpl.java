package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PluginAlertFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginAlertFieldMapper;
import com.sms.eagle.eye.backend.domain.service.PluginAlertFieldService;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigRuleResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DomainServiceAdvice
public class PluginAlertFieldServiceImpl extends ServiceImpl<PluginAlertFieldMapper,
    PluginAlertFieldEntity> implements PluginAlertFieldService {

    private final PluginAlertFieldConverter pluginAlertFieldConverter;

    public PluginAlertFieldServiceImpl(
        PluginAlertFieldConverter pluginAlertFieldConverter) {
        this.pluginAlertFieldConverter = pluginAlertFieldConverter;
    }

    @Override
    public void saveFromRpcData(List<ConfigField> list, Long pluginId) {
        if (CollectionUtils.isNotEmpty(list)) {
            saveBatch(list.stream().map(pluginConfigField -> pluginAlertFieldConverter
                    .rpcToEntity(pluginConfigField, pluginId))
                .collect(Collectors.toList()));
        }
    }

    @Override
    public List<PluginConfigRuleResponse> getResponseByPluginId(Long pluginId) {
        return list(Wrappers.<PluginAlertFieldEntity>lambdaQuery().eq(PluginAlertFieldEntity::getPluginId, pluginId))
            .stream().map(pluginAlertFieldConverter::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PluginAlertFieldEntity> getListByPluginId(Long pluginId) {
        return list(Wrappers.<PluginAlertFieldEntity>lambdaQuery()
            .eq(PluginAlertFieldEntity::getPluginId, pluginId));
    }
}