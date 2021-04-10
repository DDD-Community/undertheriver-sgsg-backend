package com.undertheriver.sgsg.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.undertheriver.sgsg.foler.domain.Folder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "user")
	private List<Folder> folders = new ArrayList<>();

	@Embedded
	private UserSecretMemoPassword userSecretMemoPassword;

	public User(String userSecretMemoPassword) {
		this.userSecretMemoPassword = UserSecretMemoPassword.from(userSecretMemoPassword);
	}
}
