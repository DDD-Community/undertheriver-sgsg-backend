package com.undertheriver.sgsg.config.security.handler;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private final String message;
    private final String jwtToken;

    @Builder
    public AuthenticationResponse(String message, String jwtToken) {
        this.message = message;
        this.jwtToken = jwtToken;
    }
}
