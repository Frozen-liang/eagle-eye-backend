package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginConfigFieldMapper;
import com.sms.eagle.eye.backend.domain.service.PluginConfigFieldService;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DomainServiceAdvice
public class PluginConfigFieldServiceImpl extends ServiceImpl<PluginConfigFieldMapper,
    PluginConfigFieldEntity> implements PluginConfigFieldService {

    private final PluginConfigFieldConverter pluginConfigFieldConverter;

    public PluginConfigFieldServiceImpl(
        PluginConfigFieldConverter pluginConfigFieldConverter) {
        this.pluginConfigFieldConverter = pluginConfigFieldConverter;
    }

    @Override
    public List<PluginConfigFieldEntity> getListByPluginId(Long pluginId) {
        return list(Wrappers.<PluginConfigFieldEntity>lambdaQuery()
            .eq(PluginConfigFieldEntity::getPluginId, pluginId));
    }

    @Override
    public List<PluginConfigFieldResponse> getResponseByPluginId(Long pluginId) {
        return getListByPluginId(pluginId).stream()
            .map(pluginConfigFieldConverter::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void saveFromRpcData(List<ConfigField> list, Long pluginId) {
        saveBatch(list.stream().map(pluginConfigField -> pluginConfigFieldConverter
                .rpcToEntity(pluginConfigField, pluginId))
            .collect(Collectors.toList()));
    }
}