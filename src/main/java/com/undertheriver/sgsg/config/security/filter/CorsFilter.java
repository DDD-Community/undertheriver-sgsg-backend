package com.undertheriver.sgsg.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CorsFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_ORIGINS = new ArrayList<>(
        Arrays.asList(
            "https://sgsg.space",
            "http://localhost:3000"
        )
    );

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String origin = request.getHeader("Origin");

        if (ALLOWED_ORIGINS.contains(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Authorization");
            response.setIntHeader("Access-Control-Max-Age", 3600);
        } else {
            logger.info("허용되지 않은 Origin입니다");
        }

        filterChain.doFilter(request, response);
    }
}
