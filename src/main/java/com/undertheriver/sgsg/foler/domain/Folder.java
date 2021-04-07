package com.undertheriver.sgsg.foler.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Getter
@Entity
public class Folder {
	@Id
	@GeneratedValue
	private Long id;

	private String title;

	@Enumerated(EnumType.STRING)
	private Color color;

	private Integer order;

	// @OneToMany(mappedBy = "folder")
	// private List<Memo> memos;
}
