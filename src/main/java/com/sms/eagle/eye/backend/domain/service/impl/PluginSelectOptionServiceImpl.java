package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PluginOptionConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginSelectOptionEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginSelectOptionMapper;
import com.sms.eagle.eye.backend.domain.service.PluginSelectOptionService;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.plugin.v1.SelectOption;
import io.vavr.control.Try;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class PluginSelectOptionServiceImpl extends ServiceImpl<PluginSelectOptionMapper, PluginSelectOptionEntity>
    implements PluginSelectOptionService {

    private static final TypeReference<List<PluginSelectOptionResponse.PluginSelectOptionItemResponse>>
        PLUGIN_SELECT_OPTION_ITEM_TYPE_REF = new TypeReference<>() {};

    private final ObjectMapper objectMapper;
    private final PluginOptionConverter pluginOptionConverter;

    public PluginSelectOptionServiceImpl(ObjectMapper objectMapper,
        PluginOptionConverter pluginOptionConverter) {
        this.objectMapper = objectMapper;
        this.pluginOptionConverter = pluginOptionConverter;
    }

    @Override
    public List<PluginSelectOptionEntity> getListByPluginId(Long pluginId) {
        return list(Wrappers.<PluginSelectOptionEntity>lambdaQuery()
            .eq(PluginSelectOptionEntity::getPluginId, pluginId));
    }

    @Override
    public List<PluginSelectOptionResponse> getResponseByPluginId(Long pluginId) {
        return getListByPluginId(pluginId).stream()
            .map(pluginSelectOptionEntity -> PluginSelectOptionResponse.builder()
                .key(pluginSelectOptionEntity.getKey())
                .data(Try.of(() -> objectMapper.readValue(
                        pluginSelectOptionEntity.getData(), PLUGIN_SELECT_OPTION_ITEM_TYPE_REF))
                    .getOrElse(Collections.emptyList()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public void saveFromRpcData(List<SelectOption> list, Long pluginId) {
        if (CollectionUtils.isNotEmpty(list)) {
            saveBatch(list.stream().map(pluginSelectOption -> pluginOptionConverter
                    .rpcToEntity(pluginSelectOption, pluginId, objectMapper))
                .collect(Collectors.toList()));
        }
    }
}