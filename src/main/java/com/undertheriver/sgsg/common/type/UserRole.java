package com.undertheriver.sgsg.common.type;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

	GUEST("ROLE_GUEST"),
	USER("ROLE_USER");

	private final String key;

	public static UserRole from(String authority) {
		return Arrays.stream(values())
			.filter(userRole -> userRole.getKey().equals(authority))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}
