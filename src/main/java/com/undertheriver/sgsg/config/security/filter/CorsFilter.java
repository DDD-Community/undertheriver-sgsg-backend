package com.undertheriver.sgsg.config.security.filter;

import static com.undertheriver.sgsg.common.exception.BadRequestException.NOT_ALLOWED_ORIGIN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.config.AppProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CorsFilter extends OncePerRequestFilter {

    private List<String> allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private Integer maxAge;

    public CorsFilter(
        AppProperties appProperties
    ) {
        allowedOrigins = appProperties.allowedOrigins();
        allowedHeaders = appProperties.allowedHeaders();
        allowedMethods = appProperties.allowedMethods();
        maxAge = appProperties.maxAge();
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String origin = request.getHeader("Origin");
            validate(origin);

            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", allowedMethods);
            response.addHeader("Access-Control-Allow-Headers", allowedHeaders);
            response.setIntHeader("Access-Control-Max-Age", maxAge);

        } catch (BadRequestException e) {
            logger.info(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void validate(String origin) {
        if (!allowedOrigins.contains(origin)) {
            String message = String.format(NOT_ALLOWED_ORIGIN, origin);
            throw new BadRequestException(message);
        }
    }
}
