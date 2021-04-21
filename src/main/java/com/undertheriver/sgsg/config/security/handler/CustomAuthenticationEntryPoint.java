package com.undertheriver.sgsg.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.undertheriver.sgsg.util.HttpResponseUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ControllerAdvice
@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		HttpResponseUtils.writeFromJson(response, new ResponseDto(authException.getMessage()));
	}

	@ExceptionHandler(value = {AccessDeniedException.class})
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException exception) throws IOException {

		response.setStatus(HttpStatus.FORBIDDEN.value());
		HttpResponseUtils.writeFromJson(response, new ResponseDto(exception.getMessage()));
	}

	@ExceptionHandler(value = {Exception.class})
	public void commence(HttpServletRequest request, HttpServletResponse response,
		Exception exception) throws IOException {

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		HttpResponseUtils.writeFromJson(response, new ResponseDto(exception.getMessage()));
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	static class ResponseDto {
		private String message;
	}
}
