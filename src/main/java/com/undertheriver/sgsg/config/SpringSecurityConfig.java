package com.undertheriver.sgsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.undertheriver.sgsg.auth.service.CustomOAuth2UserService;
import com.undertheriver.sgsg.config.security.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;

	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	private final TokenAuthenticationFilter tokenAuthenticationFilter;

	private final AuthenticationFailureHandler authenticationFailureHandler;

	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieBasedAuthorizationRequestRepository;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/css/**",
				"/js/**",
				"/img/**",
				"/lib/**",
				"/h2-console/**",
				"/v2/**",
				"/webjars/**",
				"/swagger**",
				"/swagger-resources/**",
				"/member/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.authorizeRequests()
			.antMatchers("/api/v1/auth/login").permitAll()
			.antMatchers("/callback").permitAll() // FIXME 임시, 프론트 생기면 삭제 예정
			.antMatchers("/login/oauth2/code/*").permitAll()
			.antMatchers("/oauth2/authorization/*").permitAll()
			.antMatchers("/health").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
		.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
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
		http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
