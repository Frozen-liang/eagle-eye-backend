package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.domain.entity.NotificationTemplateEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationTemplateService;
import com.sms.eagle.eye.backend.model.TemplateField;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateDetailResponse;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateResponse;
import com.sms.eagle.eye.backend.service.impl.NotificationTemplateApplicationServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class NotificationTemplateApplicationServiceTest {

    private final NotificationTemplateService notificationTemplateService = mock(NotificationTemplateService.class);
    private final NotificationTemplateApplicationService notificationTemplateApplicationService =
            new NotificationTemplateApplicationServiceImpl(notificationTemplateService);

    private static final Integer TYPE = 1;
    private static final String Template = "Template";
    private static final String DEFAULT_TEMPLATE = "";

    /**
     * {@link NotificationTemplateApplicationServiceImpl#getTemplate(Integer, Integer)} ()}
     *
     * <p> 根据 channelType通道类型 和 templateType模版类型 获取模版内容
     *
     * <p> 情形1：getTemplate 是模版内容，如果为空则返回 DEFAULT_TEMPLATE = ”“
     */
    @Test
    @DisplayName("Test the getTemplate method in the Notification Template Application Service")
    public void getTemplate_test() {
        // mock
        Optional<NotificationTemplateEntity> entity = Optional.empty();
        when(notificationTemplateService.getByChannelAndTemplateType(TYPE, TYPE)).thenReturn(entity);
        // 执行
        NotificationTemplateResponse result = notificationTemplateApplicationService.getTemplate(TYPE, TYPE);
        // 验证
        assertThat(result).isNotNull();
         assertThat(result.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
    }

    /**
     * {@link NotificationTemplateApplicationServiceImpl#getTemplate(Integer, Integer)} ()}
     *
     * <p> 根据 channelType通道类型 和 templateType模版类型 获取模版内容
     *
     * <p> 情形2：getTemplate 是模版内容，如果为不为空则返回Template
     */
    @Test
    @DisplayName("Test the getTemplate method in the Notification Template Application Service")
    public void getTemplate_test1() {
        // mock
        NotificationTemplateEntity templateEntity = mock(NotificationTemplateEntity.class);
        doReturn(Template).when(templateEntity).getTemplate();
        Optional<NotificationTemplateEntity> entityOptional = Optional.of(templateEntity);
        when(notificationTemplateService.getByChannelAndTemplateType(TYPE, TYPE)).thenReturn(entityOptional);
        // 执行
        NotificationTemplateResponse result = notificationTemplateApplicationService.getTemplate(TYPE, TYPE);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result.getTemplate()).isEqualTo(Template);
    }

    /**
     * {@link NotificationTemplateApplicationServiceImpl#getTemplateWithFieldInfo(Integer, NotificationTemplateType)} ()}
     *
     * 根据参数 channelType notificationTemplateType 获取告警通知的模版数据.
     */
    @Test
    @DisplayName("Test the getTemplateWithFieldInfo method in the Notification Template Application Service")
    public void getTemplateWithFieldInfo_test() {
        // mock
        NotificationTemplateType templateType = mock(NotificationTemplateType.class);
        Optional<NotificationTemplateEntity> entityOptional = mock(Optional.class);
        doReturn(entityOptional).when(notificationTemplateService).getByChannelAndTemplateType(TYPE,TYPE);
        List<TemplateField> fieldList = mock(List.class);
        doReturn(fieldList).when(templateType).getFieldList();
        // 执行
        NotificationTemplateDetailResponse result =
                notificationTemplateApplicationService.getTemplateWithFieldInfo(TYPE, templateType);
        // 验证
        assertThat(result).isNotNull();
        assertThat(result.getFieldList()).isEqualTo(fieldList);
    }
}
