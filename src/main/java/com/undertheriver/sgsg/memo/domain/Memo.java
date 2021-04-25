package com.undertheriver.sgsg.memo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.foler.domain.Folder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(indexes = @Index(name = "fk_folder", columnList = "folder"))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

	@Id
	@GeneratedValue
	private long id;

	@Lob
	private String content;

	private Boolean isSecret;

	private Boolean isFavorite;

	private String thumbnailUrl;

	@ManyToOne
	@JoinColumn(name = "folder", updatable = false)
	private Folder folder;
}
