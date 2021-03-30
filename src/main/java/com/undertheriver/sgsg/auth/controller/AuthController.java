package com.undertheriver.sgsg.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@PostMapping("/login")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "github-provided-code", required = true, dataType = "String", paramType = "body"),
	})
	@ApiResponses(value = {
		@ApiResponse(code = 404, message = "찾을 수 없는 유저입니다.")
	})
	public void login(@RequestBody String code) {

	}

	@DeleteMapping("/logout")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header"),
	})
	public void logout() {
	}
}
