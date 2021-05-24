package com.undertheriver.sgsg.user.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.undertheriver.sgsg.acceptance.AcceptanceTest;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.controller.dto.UserResponseDto;
import com.undertheriver.sgsg.user.domain.User;

class UserControllerTest extends AcceptanceTest {

    @DisplayName("유저 정보를 조회한다")
    @Test
    void name() {
        UserResponseDto.UserDetailResponse response = getOne("/v1/users/me",
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
        FolderPasswordRequest.CreateRequest request = new FolderPasswordRequest.CreateRequest("1234");
        String json = objectMapper.writeValueAsString(request);

        post("/v1/users/me/folder-password", json);

        UserResponseDto.UserDetailResponse response = getOne("/v1/users/me", UserResponseDto.UserDetailResponse.class);

        assertThat(response.getHasFolderPassword()).isTrue();
    }

    @DisplayName("비밀번호를 변경할 수 있다.")
    @Test
    void changeFolderPassword() throws JsonProcessingException {
        // given
        FolderPasswordRequest.CreateRequest request = new FolderPasswordRequest.CreateRequest("1234");
        String json = objectMapper.writeValueAsString(request);
        post("/v1/users/me/folder-password", json);

        //when
        FolderPasswordRequest.UpdateRequest updateRequest = new FolderPasswordRequest.UpdateRequest("1234", "4321");
        String updateJson = objectMapper.writeValueAsString(updateRequest);
        put("/v1/users/me/folder-password", updateJson);

        UserResponseDto.UserDetailResponse response = getOne("/v1/users/me", UserResponseDto.UserDetailResponse.class);

        assertThat(response.getHasFolderPassword()).isTrue();
    }

    @DisplayName("비밀번호 초기화 요청시 비밀번호가 없으면 예외가 발생한다")
    @Test
    void requestInitializePasswordException() {
        //@formatter:off
        given()
        .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth()
            .oauth2(getToken())
            .post("/v1/users/me/request-password-initialize")
        .then()
            .log()
            .all()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(containsString("이미 패스워드가 없습니다."));
        //@formatter:on
    }

    @DisplayName("비밀번호 초기화 요청")
    @Test
    void requestInitializePassword() {
        User user = getUser();
        user.createFolderPassword("1234");

        post("/v1/users/me/request-password-initialize", "");
    }

    @Test
    void initializePassword() {
        User user = getUser();
        user.createFolderPassword("1234");

        post("/v1/users/me/request-password-initialize", "");

    }

    //initializePassword
}