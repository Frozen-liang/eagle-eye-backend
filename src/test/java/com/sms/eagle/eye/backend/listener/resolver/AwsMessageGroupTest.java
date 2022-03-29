package com.sms.eagle.eye.backend.listener.resolver;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

public class AwsMessageGroupTest {

    /**
     * {@link AwsMessageGroup#resolve(String)}
     */
    @Test
    void resolve_test() {
        String name = AwsMessageGroup.ALARM.getName();
        Optional<AwsMessageGroup> optional = AwsMessageGroup.resolve(name);
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get()).isEqualTo(AwsMessageGroup.ALARM);
    }
}