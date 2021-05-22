package com.undertheriver.sgsg.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
    private final Encrypt encrypt = new Encrypt();
    private final Swagger swagger = new Swagger();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }

    @Getter
    @Setter
    public static class Encrypt {
        private String seed;
    }

    @Getter
    @Setter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    @Getter
    @Setter
    public static class Swagger {
        private List<String> serverDescription = new ArrayList<>();
        private List<String> serverUrl = new ArrayList<>();

        // public Swagger(List<String> serverDescription, List<String> serverUrl) {
        //     this.serverDescription = serverDescription;
        //     this.serverUrl = serverUrl;
        // }
    }
}