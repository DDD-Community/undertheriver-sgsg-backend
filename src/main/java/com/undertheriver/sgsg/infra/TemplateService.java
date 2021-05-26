package com.undertheriver.sgsg.infra;

import java.util.Map;

public interface TemplateService {

    String createTemplate(String templatePath, Map<String, Object> models);
}
