package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_ID_IS_NOT_CORRECT;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PluginConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.mapper.PluginMapper;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DomainServiceAdvice
public class PluginServiceImpl extends ServiceImpl<PluginMapper, PluginEntity>
    implements PluginService {

    private final PluginConverter pluginConverter;

    public PluginServiceImpl(PluginConverter pluginConverter) {
        this.pluginConverter = pluginConverter;
    }

    @Override
    public IPage<PluginResponse> getPage(PluginQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long savePluginAndReturnId(RegisterResponse registerResponse, String url) {
        PluginEntity pluginEntity = pluginConverter.rpcResponseToEntity(
            registerResponse, url, SecurityUtil.getCurrentUser().getUsername());
        save(pluginEntity);
        return pluginEntity.getId();
    }

    @Override
    public List<IdNameResponse<Long>> getList() {
        return getBaseMapper().getList();
    }

    @Override
    public void deletePlugin(Long pluginId) {
        removeById(pluginId);
    }

    @Override
    public void updatePluginStatus(Long pluginId, Boolean enabled) {
        update(Wrappers.<PluginEntity>lambdaUpdate().eq(PluginEntity::getId, pluginId)
            .set(PluginEntity::getEnabled, enabled));
    }

    @Override
    public PluginEntity getEntityById(Long pluginId) {
        Optional<PluginEntity> optional = Optional.ofNullable(getById(pluginId));
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EagleEyeException(PLUGIN_ID_IS_NOT_CORRECT);
    }
}




