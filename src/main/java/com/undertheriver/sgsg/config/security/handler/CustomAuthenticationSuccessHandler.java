package com.undertheriver.sgsg.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.undertheriver.sgsg.auth.common.JwtProvider;
import com.undertheriver.sgsg.config.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.undertheriver.sgsg.config.security.UserPrincipal;
import com.undertheriver.sgsg.util.HttpResponseUtils;
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

		HttpResponseUtils.writeFromJson(response, responseDto);

		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
