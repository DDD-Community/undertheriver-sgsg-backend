package com.undertheriver.sgsg.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.user.domain.vo.UserSecretFolderPassword;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = @Index(name = "user_idx_email", columnList = "email"))
public class User extends BaseEntity {

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private final List<UserApiClient> userApiClients = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "user")
	private List<Folder> folders = new ArrayList<>();

	@Embedded
	private UserSecretFolderPassword userSecretFolderPassword;

	@Column(nullable = false)
	private String email;

	private String profileImageUrl;

	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole userRole;

	// FIXME userSecretMemoPassword 추후 업데이트하는 형식으로 변경됨
	@Builder
	private User(String userSecretMemoPassword, String email, String profileImageUrl, String name,
		UserRole userRole) {
		this.userSecretFolderPassword = UserSecretFolderPassword.from(userSecretMemoPassword);
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.name = name;
		this.userRole = userRole;
	}

	public void saveApiClient(String oAuthId, String oAuthName) {
		if (userApiClients.stream()
			.anyMatch(userApiClient -> userApiClient.isSameOAuthName(oAuthName))) {
			return;
		}
		userApiClients.add(new UserApiClient(oAuthId, oAuthName, this));
	}

	public User update(String name, String profileImageUrl) {
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		return this;
	}

	public User delete() {
		setDeleted(true);
		return this;
	}

	public void addFolder(Folder folder) {
		this.folders.add(folder);
		folder.mapUser(this);
	}
}
