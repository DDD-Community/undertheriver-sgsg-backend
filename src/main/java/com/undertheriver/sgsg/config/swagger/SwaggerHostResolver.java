package com.undertheriver.sgsg.config.swagger;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.undertheriver.sgsg.config.AppProperties;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

@Component
public class SwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

    private List<Server> servers = new ArrayList<>();

    public SwaggerHostResolver(AppProperties appProperties) {
        AppProperties.Swagger swaggerConfig = appProperties.getSwagger();
        List<String> descriptions = swaggerConfig.getServerDescription();
        List<String> urls = swaggerConfig.getServerUrl();

        for (int i = 0; i < urls.size(); i++) {
            String description = descriptions.get(i);
            String url = swaggerConfig.getServerUrl().get(i);

            Server server = new Server();
            server.setDescription(description);
            server.setUrl(url);
            servers.add(server);
        }
    }

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openApi = context.getSpecification();
        openApi.setServers(servers);
        return openApi;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return delimiter.equals(DocumentationType.OAS_30);
    }
}
