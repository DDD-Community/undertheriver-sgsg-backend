package com.undertheriver.sgsg.foler.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.memo.domain.Memo;

import lombok.AccessLevel;
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

	private Integer order;

	@OneToMany(mappedBy = "folder")
	private List<Memo> memos = new ArrayList<>();
}
