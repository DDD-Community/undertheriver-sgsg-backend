package com.undertheriver.sgsg.user.exception;

public class PasswordValidationException extends RuntimeException {
    public static final String NO_PASSWORD = "패스워드가 등록되어 있지 않습니다";
    public static final String PASSWORD_NOT_MATCH = "패스워드가 일치하지 않습니다";

    public PasswordValidationException(String message) {
        super(message);
    }
}
