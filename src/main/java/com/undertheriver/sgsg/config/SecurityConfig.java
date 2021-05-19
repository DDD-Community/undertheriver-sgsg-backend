package com.undertheriver.sgsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.undertheriver.sgsg.auth.service.CustomOAuth2UserService;
import com.undertheriver.sgsg.config.security.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieBasedAuthorizationRequestRepository;

    private final AccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/v3/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
		http.authorizeRequests()
			.antMatchers("/login/oauth2/code/*").permitAll()
			.antMatchers("/oauth2/authorization/*").permitAll()
			.antMatchers("/health").permitAll()
			.anyRequest().authenticated()
		.and()
			.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler)
		.and()
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.csrf()
				.disable()
			.formLogin()
				.disable()
			.httpBasic()
				.disable()
			.logout()
			.logoutSuccessUrl("/")
		.and()
			.oauth2Login()
				.authorizationEndpoint()
					.authorizationRequestRepository(cookieBasedAuthorizationRequestRepository)
					.and()
				.userInfoEndpoint()
					.userService(customOAuth2UserService)
					.and()
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler);
		//@formatter:on
    }
}
