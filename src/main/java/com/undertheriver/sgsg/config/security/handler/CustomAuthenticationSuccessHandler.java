package com.undertheriver.sgsg.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.undertheriver.sgsg.auth.common.JwtProvider;
import com.undertheriver.sgsg.config.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.undertheriver.sgsg.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		String token = jwtProvider.createToken(userPrincipal.getId(), userPrincipal.fetchAuthority());
		AuthenticationResponse responseDto = AuthenticationResponse.builder()
			.jwtToken(token)
			.build();

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		MediaType jsonMimeType = MediaType.APPLICATION_JSON;

		if (jsonConverter.canWrite(responseDto.getClass(), jsonMimeType)) {
			jsonConverter.write(responseDto, jsonMimeType, new ServletServerHttpResponse(response));
		}

		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
