package com.undertheriver.sgsg.user.domain.vo;

import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.undertheriver.sgsg.user.domain.PasswordConverter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSecretFolderPassword {

	@Convert(converter = PasswordConverter.class)
	private String password;

	private UserSecretFolderPassword(String password) {
		this.password = password;
	}

	public static UserSecretFolderPassword from(String password) {
		return new UserSecretFolderPassword(password);
	}

	public boolean isEmpty() {
		return Objects.isNull(password)
			|| password.isEmpty();
	}
}
