package com.undertheriver.sgsg.config;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BCryptPasswordEncoderConfig {

    private final AppProperties appProperties;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        String seed = appProperties.getEncrypt()
            .getSeed();
        return new BCryptPasswordEncoder(4, new SecureRandom(seed.getBytes(StandardCharsets.UTF_8)));
    }
}
