package com.undertheriver.sgsg.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.undertheriver.sgsg.core.ApiResult;
import com.undertheriver.sgsg.user.exception.PasswordCreateFailException;
import com.undertheriver.sgsg.user.exception.PasswordValidationException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class UserControllerAdvice {

    @ExceptionHandler(value = {PasswordCreateFailException.class, PasswordValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleBadRequest(PasswordCreateFailException e) {
        log.info("PasswordCreateFailException, message: {{}}", e.getMessage());
        return ApiResult.ERROR(e, HttpStatus.BAD_REQUEST);
    }
}
