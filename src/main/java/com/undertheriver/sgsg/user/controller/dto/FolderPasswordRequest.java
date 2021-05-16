package com.undertheriver.sgsg.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class FolderPasswordRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateRequest {
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateRequest {
        private String currentPassword;
        private String newPassword;
    }
}
