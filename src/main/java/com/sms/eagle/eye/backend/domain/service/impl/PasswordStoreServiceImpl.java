package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PASSWORD_KEY_IS_NOT_EXIST;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PasswordStoreConverter;
import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.domain.mapper.PasswordStoreMapper;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class PasswordStoreServiceImpl extends ServiceImpl<PasswordStoreMapper, PasswordStoreEntity>
    implements PasswordStoreService {

    public static final String PASSWORD_REGEX = "EAGLE#(.*?)#VAULT";
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);
    public static final Integer PASSWORD_KEY_GROUP = 1;

    private static final String KEY_FORMAT = "EAGLE#%s#VAULT";

    private final StringEncryptor stringEncryptor;
    private final PasswordStoreConverter passwordStoreConverter;

    public PasswordStoreServiceImpl(StringEncryptor stringEncryptor,
        PasswordStoreConverter passwordStoreConverter) {
        this.stringEncryptor = stringEncryptor;
        this.passwordStoreConverter = passwordStoreConverter;
    }

    @Override
    public Integer countByKey(String key) {
        return getBaseMapper().selectCountByKey(key.toLowerCase(Locale.ROOT));
    }

    @Override
    public IPage<PasswordPageResponse> getPage(PasswordQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Override
    public void saveFromRequest(PasswordRequest request) {
        PasswordStoreEntity entity = passwordStoreConverter.toEntity(request, stringEncryptor);
        entity.setCreator(SecurityUtil.getCurrentUser().getUsername());
        save(entity);
    }

    @Override
    public void updateFromRequest(PasswordRequest request) {
        updateById(passwordStoreConverter.toEntity(request, stringEncryptor));
    }

    @Override
    public void deletePasswordById(Long id) {
        removeById(id);
    }

    @Override
    public List<PasswordSelectResponse> getList() {
        return list().stream().map(passwordStoreEntity -> PasswordSelectResponse.builder()
                .key(String.format(KEY_FORMAT, passwordStoreEntity.getKey()))
                .description(passwordStoreEntity.getDescription())
                .creator(passwordStoreEntity.getCreator())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public String getValueByKey(String key) {
        Optional<String> optional = getBaseMapper().getValueByKey(key.toLowerCase(Locale.ROOT));
        return stringEncryptor.decrypt(
            optional.orElseThrow(() -> new EagleEyeException(PASSWORD_KEY_IS_NOT_EXIST)));
    }
}




