package com.undertheriver.sgsg.foler.domain;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.undertheriver.sgsg.foler.repository.FolderRepository;

public enum FolderOrderBy {
	NAME(Sort.by(Sort.Direction.ASC,"title")),
	CREATED_AT(Sort.by(Sort.Direction.ASC,"createdAt")),
	MEMO(null);

	protected final Sort sort;

	FolderOrderBy(Sort sort) {
		this.sort = sort;
	}

	public List<Folder> findFolders(Long userId, FolderRepository folderRepository) {
		switch (this) {
			case NAME:
			case CREATED_AT:
				return folderRepository.findAllByUserId(userId, sort);
			case MEMO:
				return folderRepository.findAllOrderByMemos(userId);
			default:
				return Arrays.asList(new Folder());
		}
	}
}
