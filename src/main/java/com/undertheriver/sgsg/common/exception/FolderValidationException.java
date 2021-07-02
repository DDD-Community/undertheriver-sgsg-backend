package com.undertheriver.sgsg.common.exception;

public class FolderValidationException extends RuntimeException {
    public static final String DUPLICATE_FOLDER_NAME = "이미 존재하는 폴더 이름입니다.";
    public static final String TOO_MANY_FOLDER = "폴더를 더 이상 생성할 수 없습니다.";
    public FolderValidationException(String message) {
        super(message);
    }
}
