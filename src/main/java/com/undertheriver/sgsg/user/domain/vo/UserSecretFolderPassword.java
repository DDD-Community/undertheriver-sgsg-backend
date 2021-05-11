package com.undertheriver.sgsg.user.domain.vo;

import java.util.Objects;

import javax.persistence.Embeddable;

import com.undertheriver.sgsg.user.exception.PasswordCreateFailException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSecretFolderPassword {

    private String encryptedPassword;

    private UserSecretFolderPassword(String encryptedPassword) {
        validate(encryptedPassword);
        this.encryptedPassword = encryptedPassword;
    }

    private void validate(String encryptedPassword) {
        if (Objects.isNull(encryptedPassword) || encryptedPassword.isEmpty()) {
            throw new PasswordCreateFailException();
        }
    }

    public static UserSecretFolderPassword from(String encryptedPassword) {
        return new UserSecretFolderPassword(encryptedPassword);
    }

    public boolean isEmpty() {
        return Objects.isNull(encryptedPassword)
            || encryptedPassword.isEmpty();
    }
}
