package com.undertheriver.sgsg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.authorizeRequests()
			.antMatchers("/health").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
		.and()
			.csrf()
			.disable();
		//@formatter:on
	}
}
