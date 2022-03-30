package com.sms.eagle.eye.backend.nerko;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_OAUTH_RESOURCE_ERROR;
import com.sms.eagle.eye.backend.nerko.client.CoordinationClient;
import com.sms.eagle.eye.backend.nerko.dto.NerkoProjectInfo;
import com.sms.eagle.eye.backend.nerko.response.NerkoBaseResponse;
import com.sms.eagle.eye.backend.nerko.service.NerkoCoordinationService;
import com.sms.eagle.eye.backend.nerko.service.impl.NerkoCoordinationServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;

public class NerkoCoordinationServiceTest {

    private final CoordinationClient coordinationClient = mock(CoordinationClient.class);
    private final NerkoCoordinationService service = spy(new NerkoCoordinationServiceImpl(coordinationClient));

    @Test
    public void getProjectList_test() {
        // mock
        NerkoBaseResponse<List<NerkoProjectInfo>> response = mock(NerkoBaseResponse.class);
        NerkoProjectInfo projectInfo = mock(NerkoProjectInfo.class);
        List<NerkoProjectInfo> list = Arrays.asList(projectInfo);
        when(response.getData()).thenReturn(list);
        when(coordinationClient.getProjectList()).thenReturn(response);
        // 执行
        List<NerkoProjectInfo> result = service.getProjectList();
        // 验证
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(list);
    }

    @Test
    public void getProjectList_exception_test() {
        // mock
        when(coordinationClient.getProjectList()).thenThrow(EagleEyeException.class);
        // 验证异常
        assertThatThrownBy(service::getProjectList).isInstanceOf(EagleEyeException.class)
                .extracting("code")
                .isEqualTo(GET_OAUTH_RESOURCE_ERROR.getCode());
    }
}
