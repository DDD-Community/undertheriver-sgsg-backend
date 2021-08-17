package com.undertheriver.sgsg.common.exception;

public class BadRequestException extends RuntimeException {
    public static final String UNMACHED_USER = "유저 관계가 일치하지 않습니다.";
    public static final String NOT_ALLOWED_ORIGIN = "CORS를 위반한 요청입니다. Origin: %s";
    public BadRequestException(String msg) { super(msg); }
}
