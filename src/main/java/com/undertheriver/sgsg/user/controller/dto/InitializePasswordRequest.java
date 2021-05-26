package com.undertheriver.sgsg.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InitializePasswordRequest {
    private String key;
}
