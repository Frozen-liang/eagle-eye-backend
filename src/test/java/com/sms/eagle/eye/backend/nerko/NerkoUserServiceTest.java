package com.sms.eagle.eye.backend.nerko;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_OAUTH_RESOURCE_ERROR;
import com.sms.eagle.eye.backend.nerko.client.UserManagementClient;
import com.sms.eagle.eye.backend.nerko.dto.NerkoUserInfo;
import com.sms.eagle.eye.backend.nerko.response.NerkoBaseResponse;
import com.sms.eagle.eye.backend.nerko.service.NerkoUserService;
import com.sms.eagle.eye.backend.nerko.service.impl.NerkoUserServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;

public class NerkoUserServiceTest {

    private final UserManagementClient userManagementClient = mock(UserManagementClient.class);
    private final NerkoUserService service = spy(new NerkoUserServiceImpl(userManagementClient));

    @Test
    public void getUserList_test(){
        // mock
        NerkoBaseResponse<List<NerkoUserInfo>> response = mock(NerkoBaseResponse.class);
        when(userManagementClient.getUserList()).thenReturn(response);
        NerkoUserInfo info = mock(NerkoUserInfo.class);
        List<NerkoUserInfo> list = Arrays.asList(info);
        when(response.getData()).thenReturn(list);
        // 执行
        List<NerkoUserInfo> result = service.getUserList();
        // 验证
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(list);
    }

    @Test
    public void getProjectList_exception_test() {
        // mock
        when(userManagementClient.getUserList()).thenThrow(EagleEyeException.class);
        // 验证异常
        assertThatThrownBy(service::getUserList).isInstanceOf(EagleEyeException.class)
                .extracting("code")
                .isEqualTo(GET_OAUTH_RESOURCE_ERROR.getCode());
    }
}
