package com.undertheriver.sgsg.user.domain;

import java.util.Arrays;

public enum OAuthName {
	GOOGLE;

	public static OAuthName of(String oAuthName) {
		return Arrays.stream(values())
			.filter(it -> it.name().equalsIgnoreCase(oAuthName))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 인증타입니다."));
	}

	public boolean isSame(String oAuthName) {
		return name().equalsIgnoreCase(oAuthName);
	}
}
