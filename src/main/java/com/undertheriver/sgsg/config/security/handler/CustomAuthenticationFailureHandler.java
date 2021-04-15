package com.undertheriver.sgsg.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.undertheriver.sgsg.config.security.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(400);
		AuthenticationResponse responseDto = AuthenticationResponse.builder()
			.message(exception.getMessage())
			.build();

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		MediaType jsonMimeType = MediaType.APPLICATION_JSON;

		if (jsonConverter.canWrite(responseDto.getClass(), jsonMimeType)) {
			jsonConverter.write(responseDto, jsonMimeType, new ServletServerHttpResponse(response));
		}

		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
