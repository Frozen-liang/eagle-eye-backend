package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordStoreConverterTest {

    private final PasswordStoreConverterImpl converter = new PasswordStoreConverterImpl();
    private final PasswordRequest request = mock(PasswordRequest.class);
    private final StringEncryptor encryptor = mock(StringEncryptor.class);

    private final Long ID = 1L;
    private final String KEY = "KEY";
    private final String VAULE = "VAULE";

    @Test
    void toEntity_test1() {
        assertThat(converter.toEntity(null, encryptor)).isNull();
    }

    @Test
    void toEntity_test2() {
        // mock
        when(converter.encrypt(VAULE, encryptor)).thenReturn(VAULE);
        when(request.getValue()).thenReturn(VAULE);
        when(request.getId()).thenReturn(ID);
        PasswordStoreEntity result = converter.toEntity(request, encryptor);
        assertThat(result.getValue()).isEqualTo(VAULE);
        assertThat(result.getId()).isEqualTo(ID);
    }
}
