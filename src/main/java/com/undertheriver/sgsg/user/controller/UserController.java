package com.undertheriver.sgsg.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;
import com.undertheriver.sgsg.core.ApiResult;
import com.undertheriver.sgsg.user.controller.dto.UserDto;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/me")
@Api(value = "User")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "회원 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping
	public ApiResult<UserDto.DetailResponse> user(@LoginUserId Long userId) {
		User user = userService.findById(userId);

		UserDto.DetailResponse userDetail = new UserDto.DetailResponse(user.getName(),
			user.getEmail(), user.getProfileImageUrl(), user.hasFolderPassword());

		return ApiResult.OK(userDetail);
	}

	@ApiOperation(value = "회원 탈퇴")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header")
	})
	@DeleteMapping
	public ApiResult<Void> deleteUser(@LoginUserId Long userId) {
		userService.deleteUser(userId);

		return ApiResult.OK();
	}

	@ApiOperation(value = "메모비밀번호 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Bearer sgsg-token-value", required = true, dataType = "String", paramType = "header"),
		@ApiImplicitParam(name = "password", value = "1234", required = true, dataType = "String", paramType = "body")
	})
	@PutMapping("/memo-password")
	public void updateMemoPassword(@RequestBody String password) {
	}
}
