package com.undertheriver.sgsg.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserDetailResponse {
        private String name;
        private String email;
        private String profileImageUrl;
        private Boolean hasFolderPassword;
    }
}
