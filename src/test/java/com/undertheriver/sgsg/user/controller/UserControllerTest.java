package com.undertheriver.sgsg.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.undertheriver.sgsg.acceptance.AcceptanceTest;
import com.undertheriver.sgsg.user.controller.dto.UserDto;

class UserControllerTest extends AcceptanceTest {

    @DisplayName("유저 정보를 조회한다")
    @Test
    void name() {
        UserDto.DetailResponse response = getOne("/api/v1/users/me", UserDto.DetailResponse.class);

        assertAll(
            () -> assertThat(response.getName()).isEqualTo("TEST"),
            () -> assertThat(response.getEmail()).isEqualTo("test@test.com"),
            () -> assertThat(response.getProfileImageUrl()).isEqualTo("http://naver.com/adf.png")
        );
    }

    @DisplayName("회원을 탈퇴한다")
    @Test
    public void dummy() {
        delete("/api/v1/users/me");
    }
}