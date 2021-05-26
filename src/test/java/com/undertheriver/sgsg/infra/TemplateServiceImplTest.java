package com.undertheriver.sgsg.infra;

import static com.undertheriver.sgsg.infra.MailService.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TemplateServiceImplTest {

    @Test
    void createTemplate() {
        TemplateServiceImpl templateService = new TemplateServiceImpl();

        String template = templateService.createTemplate(DEFAULT_PATH, Map.of("key", "test"));

        assertThat(template).contains("https://sgsg.space/initialize-password?key=test");
    }
}