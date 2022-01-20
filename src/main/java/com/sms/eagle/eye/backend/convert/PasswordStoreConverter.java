package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import org.jasypt.encryption.StringEncryptor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PasswordStoreConverter {

    @Mapping(target = "value", qualifiedByName = "encrypt")
    PasswordStoreEntity toEntity(PasswordRequest request, @Context StringEncryptor stringEncryptor);

    @Named("encrypt")
    default String encrypt(String value, @Context StringEncryptor stringEncryptor) {
        return stringEncryptor.encrypt(value);
    }

}
