package com.undertheriver.sgsg.common.dto;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public final class CurrentUser {
	private Long id;
	private String name;
	private String profileImageUrl;
	private UserRole userRole;

	@Builder
	private CurrentUser(Long id, String name, String profileImageUrl, UserRole userRole) {
		this.id = id;
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		this.userRole = userRole;
	}
}
