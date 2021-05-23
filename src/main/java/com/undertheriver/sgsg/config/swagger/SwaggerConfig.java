package com.undertheriver.sgsg.config.swagger;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .ignoredParameterTypes(LoginUserId.class)
            .securitySchemes(List.of(apiKey()))
            .useDefaultResponseMessages(false)
            .securityContexts(List.of(securityContext()))
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .operationSelector(operationContext -> true)
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList((new SecurityReference("JWT", authorizationScopes)));
    }

    private HttpAuthenticationScheme apiKey() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("JWT").build();
    }
}