package com.undertheriver.sgsg.foler.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Folder extends BaseEntity {
	@Id
	@GeneratedValue
	private Long id;

	private String title;

	@Enumerated(EnumType.STRING)
	private FolderColor color;

	private Integer position;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false)
	private User user;

	@OneToMany(mappedBy = "folder")
	private List<Memo> memos = new ArrayList<>();

	@Builder
	public Folder(String title, FolderColor color, Integer position, User user) {
		this.title = title;
		this.color = color;
		this.position = position;
		this.user = user;
	}

	public void updateTitle(FolderDto.UpdateFolderTitleReq dto) {
		this.title = dto.getTitle();
	}

	public void updatePosition(FolderDto.UpdateFolderPositionReq dto) {
		this.position = dto.getPosition();
	}

	public void update(FolderDto.UpdateFolderReq dto) {
		this.title = dto.getTitle();
		this.color = dto.getColor();
		this.position = dto.getPosition();
	}
}