package com.undertheriver.sgsg.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.common.annotation.LoginUserId;
import com.undertheriver.sgsg.core.ApiResult;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.controller.dto.UserRequestDto;
import com.undertheriver.sgsg.user.controller.dto.UserResponseDto;
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
    @GetMapping
    public ApiResult<UserResponseDto.UserDetailResponse> user(@LoginUserId Long userId) {
        User user = userService.findById(userId);

        UserResponseDto.UserDetailResponse userDetail = new UserResponseDto.UserDetailResponse(user.getName(),
            user.getEmail(), user.getProfileImageUrl(), user.hasFolderPassword());

        return ApiResult.OK(userDetail);
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping
    public ApiResult<Void> deleteUser(@LoginUserId Long userId) {
        userService.deleteUser(userId);

        return ApiResult.OK();
    }

    @ApiOperation(value = "메모 비밀번호 생성")
    @PostMapping("/folder-password")
    public ApiResult<Object> createFolderPassword(
        @LoginUserId Long userId, @RequestBody FolderPasswordRequest.CreateRequest request
    ){
        userService.createFolderPassword(userId, request);
        return ApiResult.OK();
    }

    @ApiOperation(value = "메모비밀번호 변경")
    @PutMapping("/folder-password")
    public ApiResult<Object> updateFolderPassword(
        @LoginUserId Long userId, @RequestBody FolderPasswordRequest.UpdateRequest request
    ) {
        userService.updateFolderPassword(userId, request);
        return ApiResult.OK();
    }
}
