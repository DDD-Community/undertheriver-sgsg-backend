package com.undertheriver.sgsg.common.exception;

public class BadRequestException extends RuntimeException {
    public static final String UNMACHED_USER = "유저 관계가 일치하지 않습니다.";
    public BadRequestException(String msg) { super(msg); }
}
