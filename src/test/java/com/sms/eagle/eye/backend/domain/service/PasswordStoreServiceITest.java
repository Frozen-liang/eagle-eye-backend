package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.convert.PasswordStoreConverter;
import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.domain.mapper.PasswordStoreMapper;
import com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import static org.assertj.core.api.Assertions.assertThat;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PasswordStoreServiceITest {

    private final StringEncryptor stringEncryptor = mock(StringEncryptor.class);
    private final PasswordStoreConverter passwordStoreConverter = mock(PasswordStoreConverter.class);
    private final PasswordStoreMapper storeMapper = mock(PasswordStoreMapper.class);
    private final PasswordQueryRequest queryRequest = mock(PasswordQueryRequest.class);
    private final PasswordRequest passwordRequest = mock(PasswordRequest.class);
    private final StringEncryptor encryptor = mock(StringEncryptor.class);
    private final PasswordStoreEntity storeEntity = mock(PasswordStoreEntity.class);
    private final PasswordStoreService passwordStoreService =
            spy(new PasswordStoreServiceImpl(stringEncryptor, passwordStoreConverter));

    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";
    private static final Long ID = 1L;

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        UserInfo userInfo = UserInfo.builder().username("username").nickname("nickname").email("email@test.com")
                .build();
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);
    }

    @AfterAll
    public static void close(){
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test the countByKey method in the Password Store ServiceI")
    public void countByKey_test(){
        doReturn(storeMapper).when(passwordStoreService).getBaseMapper();
        when(storeMapper.selectCountByKey(KEY)).thenReturn(0);
        assertThat(passwordStoreService.countByKey(KEY)).isEqualTo(0);
    }

    @Test
    @DisplayName("Test the getPage method in the Password Store ServiceI")
    public void getPage_test(){
        when(queryRequest.getPageInfo()).thenReturn(new Page<>());
        doReturn(storeMapper).when(passwordStoreService).getBaseMapper();
        when(storeMapper.getPage(any(),any())).thenReturn(new Page<>());
        assertThat(passwordStoreService.getPage(queryRequest)).isNotNull();
    }

    @Test
    @DisplayName("Test the saveFromRequest method in the Password Store ServiceI")
    public void saveFromRequest_test(){
        when(passwordStoreConverter.toEntity(any(),any())).thenReturn(storeEntity);
        doNothing().when(storeEntity).setCreator(VALUE);
        doReturn(true).when(passwordStoreService).save(storeEntity);
        passwordStoreService.saveFromRequest(passwordRequest);
        verify(passwordStoreService).saveFromRequest(any());
    }

    @Test
    @DisplayName("Test the updateFromRequest method in the Password Store ServiceI")
    public void updateFromRequest_test(){
        when(passwordStoreConverter.toEntity(passwordRequest,stringEncryptor)).thenReturn(storeEntity);
        doReturn(Boolean.TRUE).when(passwordStoreService).updateById(storeEntity);
        passwordStoreService.updateFromRequest(passwordRequest);
        verify(passwordStoreService).updateById(storeEntity);
    }

    @Test
    @DisplayName("Test the deletePasswordById method in the Password Store ServiceI")
    public void deletePasswordById_test(){
        doReturn(Boolean.TRUE).when(passwordStoreService).removeById(any(Serializable.class));
        passwordStoreService.deletePasswordById(ID);
        verify(passwordStoreService).deletePasswordById(ID);
    }

    @Test
    @DisplayName("Test the getList method in the Password Store ServiceI")
    public void getList_test(){
        List<PasswordSelectResponse> list = mock(List.class);
        doReturn(list).when(passwordStoreService).list(any(Wrapper.class));
        when(storeEntity.getKey()).thenReturn(KEY);
        passwordStoreService.getList();
        assertThat(passwordStoreService.getList()).hasSize(0);
    }

    @Test
    @DisplayName("Test the getValueByKey method in the Password Store ServiceI")
    public void getValueByKey_test(){
        doReturn(storeMapper).when(passwordStoreService).getBaseMapper();
        when(storeMapper.getValueByKey(KEY.toLowerCase(Locale.ROOT))).thenReturn(Optional.of(VALUE));
        when(stringEncryptor.decrypt(VALUE)).thenReturn(VALUE);
        assertThat(passwordStoreService.getValueByKey(KEY)).isNotNull();

    }
}
