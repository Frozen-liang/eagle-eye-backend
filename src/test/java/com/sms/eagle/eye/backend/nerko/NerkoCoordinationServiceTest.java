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
import java.util.Objects;

public class NerkoCoordinationServiceTest {

    private final CoordinationClient coordinationClient = mock(CoordinationClient.class);
    private final NerkoCoordinationService service = spy(new NerkoCoordinationServiceImpl(coordinationClient));

    /**
     * {@link NerkoCoordinationService#getProjectList()}.
     *
     * <p>获取 项目列表
     */
    @Test
    public void getProjectList_test() {
        // 请求构建参数
        String NAME = "NAME";
        // mock
        NerkoProjectInfo projectInfo = mock(NerkoProjectInfo.class);

        List<NerkoProjectInfo> list = Arrays.asList(projectInfo);

        NerkoBaseResponse<List<NerkoProjectInfo>> response = mock(NerkoBaseResponse.class);
        when(response.getData()).thenReturn(list);
        when(projectInfo.getName()).thenReturn(NAME);
        when(coordinationClient.getProjectList()).thenReturn(response);
        // 执行
        List<String> projectList = service.getProjectList();
        // 验证
        assertThat(projectList).isNotEmpty();
        assertThat(projectList).allMatch(item -> Objects.equals(item, NAME));
    }

    /**
     * {@link NerkoCoordinationService#getProjectList()}.
     *
     * <p>获取 项目列表异常
     */
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
