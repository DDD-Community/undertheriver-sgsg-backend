package com.undertheriver.sgsg.infra;

import static com.undertheriver.sgsg.infra.MailService.*;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateServiceImpl implements TemplateService {

    private static final String INITIALIZE_PASSWORD_TEMPLATE = "<html><p>비밀번호 초기화 메일입니다. 아래 링크를 클릭해주세요.</p>%s</html>";

    @Override
    public String createTemplate(String templatePath, Map<String, Object> models) {
        if (templatePath.equals(DEFAULT_PATH)) {
            return initializePasswordTemplate(models);
        }
        throw new IllegalArgumentException("잘못된 템플릿을 입력했습니다.");
    }

    private String initializePasswordTemplate(Map<String, Object> models) {
        String redirectUrl = "https://sgsg.space/initialize-password?key=" + models.get("key");
        String redirectUrlLink = String.format("<a href='%s'>%s</a>",
            redirectUrl,
            redirectUrl
        );

        return String.format(INITIALIZE_PASSWORD_TEMPLATE, redirectUrlLink);
    }
}
