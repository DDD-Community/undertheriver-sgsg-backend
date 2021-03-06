package com.undertheriver.sgsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/**")
            .allowedOrigins("http://localhost:3000", "https://sgsg.name","chrome-extension://mbaagcnmdjpechkaefcpcnbnmjbeddpc/popup.html", "chrome-extension://cbcfldfiodebkafgjhiokikamikajekn/popup.html")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Authorization", "Content-Type")
            .allowCredentials(true)
            .maxAge(3600L);
    }
}
