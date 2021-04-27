package com.undertheriver.sgsg.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUser;
import com.undertheriver.sgsg.common.dto.CurrentUser;
import com.undertheriver.sgsg.user.controller.dto.UserDto;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "회원 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping("/me")
	public ResponseEntity<UserDto.DetailResponse> user(@LoginUser CurrentUser currentUser) {
		User user = userService.findById(currentUser.getId());

		UserDto.DetailResponse userDetail = new UserDto.DetailResponse(user.getName(),
			user.getEmail(), user.getProfileImageUrl());

		return ResponseEntity.ok(userDetail);
	}

	@ApiOperation(value = "회원 탈퇴")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header")
	})
	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteUser(@LoginUser CurrentUser currentUser) {
		userService.deleteUser(currentUser.getId());

		return ResponseEntity.ok()
			.build();
	}

	@ApiOperation(value = "메모비밀번호 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header"),
		@ApiImplicitParam(name = "password", value = "1234", required = true, dataType = "String", paramType = "body")
	})
	@PatchMapping
	public void updateMemoPassword(@RequestBody String password) {
	}
}
