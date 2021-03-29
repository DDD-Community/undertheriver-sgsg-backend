package com.undertheriver.sgsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

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
			.antMatchers("/health").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
		.and()
			.csrf()
			.disable();
		//@formatter:on
	}
}
