package com.undertheriver.sgsg.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class changePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
