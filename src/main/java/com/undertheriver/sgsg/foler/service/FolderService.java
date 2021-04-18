package com.undertheriver.sgsg.foler.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.dto.CurrentUser;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FolderService {
	private final FolderRepository folderRepository;

	private static final Integer FOLDER_INDEX_LIMIT = 20;

	@Transactional
	public Long save(Long userId, FolderDto.CreateFolderReq req) {
		if (isFoldersExistsMoreThan20(userId)) {
			throw new IndexOutOfBoundsException("폴더는 최대 20개까지 생성할 수 있습니다!");
		}
		return folderRepository.save(req.toEntity()).getId();
	}

	@Transactional(readOnly = true)
	boolean isFoldersExistsMoreThan20(Long userId) {
		return read(userId).size() >= FOLDER_INDEX_LIMIT;
	}

	@Transactional(readOnly = true)
	public List<FolderDto.ReadFolderRes> read(Long userId) {
		return folderRepository.findFirst20ByUserIdAndDeletedFalseOrDeletedNull(userId)
			.stream()
			.map(FolderDto.ReadFolderRes::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public List<FolderDto.ReadFolderRes> update(List<FolderDto.UpdateFolderReq> body) {
		return body.stream()
			.map(this::updateFolder)
			.map(FolderDto.ReadFolderRes::toDto)
			.collect(Collectors.toList());
	}

	private Folder updateFolder(FolderDto.UpdateFolderReq dto) {
		final Folder folder = folderRepository.findById(dto.getId()).get();
		folder.update(dto);
		return folder;
	}
}
