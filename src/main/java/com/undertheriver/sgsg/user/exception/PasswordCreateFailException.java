package com.undertheriver.sgsg.user.exception;

public class PasswordCreateFailException extends RuntimeException {

    private static final String HAS_PASSWORD = "이미 등록된 패스워드가 있습니다.";

    public PasswordCreateFailException() {
        super(HAS_PASSWORD);
    }
}
