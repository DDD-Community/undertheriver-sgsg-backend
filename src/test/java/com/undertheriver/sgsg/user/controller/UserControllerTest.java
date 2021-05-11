package com.undertheriver.sgsg.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.undertheriver.sgsg.acceptance.AcceptanceTest;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.controller.dto.UserResponseDto;

class UserControllerTest extends AcceptanceTest {

    @DisplayName("유저 정보를 조회한다")
    @Test
    void name() {
        UserResponseDto.UserDetailResponse response = getOne("/api/v1/users/me",
            UserResponseDto.UserDetailResponse.class);

        assertAll(
            () -> assertThat(response.getName()).isEqualTo("TEST"),
            () -> assertThat(response.getEmail()).isEqualTo("test@test.com"),
            () -> assertThat(response.getProfileImageUrl()).isEqualTo("http://naver.com/adf.png")
        );
    }


    @DisplayName("초기 비밀번호를 설정할 수 있다.")
    @Test
    void name1() throws JsonProcessingException {
        FolderPasswordRequest request = new FolderPasswordRequest("1234");
        String json = objectMapper.writeValueAsString(request);

        post("/api/v1/users/me/folder-password", json);

        UserResponseDto.UserDetailResponse response = getOne("/api/v1/users/me", UserResponseDto.UserDetailResponse.class);

        assertThat(response.getHasFolderPassword()).isTrue();
    }

}