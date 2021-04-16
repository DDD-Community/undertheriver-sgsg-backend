package com.undertheriver.sgsg.auth.service.dto;

import java.util.Map;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private final String oAuthName;

	private final String oAuthIdKey;

	private final String name;

	private final String profileImageUrl;

	private final String email;

	private final Map<String, Object> attributes;

	@Builder
	protected OAuthAttributes(String oAuthName, String oAuthIdKey, String name, String profileImageUrl, String email,
		Map<String, Object> attributes) {
		this.oAuthName = oAuthName;
		this.oAuthIdKey = oAuthIdKey;
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		this.email = email;
		this.attributes = attributes;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {

		return builder()
			.oAuthName(registrationId)
			.oAuthIdKey(userNameAttributeName)
			.name((String)attributes.get("name"))
			.profileImageUrl((String)attributes.get("picture"))
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.build();
	}

	public User toEntity() {
		return User.builder()
			.email(email)
			.userRole(UserRole.USER)
			.name(name)
			.profileImageUrl(profileImageUrl)
			.build();
	}

	public String fetchOAuthId() {
		return (String)attributes.get(oAuthIdKey);
	}
}
