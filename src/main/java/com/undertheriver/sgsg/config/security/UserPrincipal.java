package com.undertheriver.sgsg.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.domain.User;

public class UserPrincipal implements OAuth2User, UserDetails {

	private final Long id;
	private final String name;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;
	private final Map<String, Object> attributes;

	private UserPrincipal(Long id, String name, String email,
		Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes) {

		this.id = id;
		this.name = name;
		this.email = email;
		this.authorities = authorities;
		this.attributes = attributes;
	}

	public static UserPrincipal create(User user) {
		return create(user, null);
	}

	public static UserPrincipal create(User user, Map<String, Object> attributes) {
		List<GrantedAuthority> authorities = Collections.
			singletonList(new SimpleGrantedAuthority(user.getUserRole().getKey()));

		return new UserPrincipal(
			user.getId(),
			user.getName(),
			user.getEmail(),
			authorities,
			attributes
		);
	}

	public UserRole fetchAuthority() {
		GrantedAuthority grantedAuthority = authorities.stream()
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);

		return UserRole.from(grantedAuthority.getAuthority());
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return name;
	}
}
