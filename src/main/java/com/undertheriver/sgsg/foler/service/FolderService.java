package com.undertheriver.sgsg.foler.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.dto.PageRequest;
import com.undertheriver.sgsg.config.PagingConfig;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FolderService {
	private final FolderRepository folderRepository;
	private final PagingConfig pagingConfig;

	@Transactional
	public Long save(Long userId, FolderDto.CreateFolderReq req) {
		Integer limit = pagingConfig.getFolderConfig().get("limit");

		if (isFoldersExistsMoreThan20(userId, limit)) {
			throw new IndexOutOfBoundsException(
				String.format("폴더는 최대 %d개까지 생성할 수 있습니다!", limit));
		}
		return folderRepository.save(req.toEntity()).getId();
	}

	private boolean isFoldersExistsMoreThan20(Long userId, Integer limit) {
		return read(userId).size() >= limit;
	}

	@Transactional(readOnly = true)
	public List<FolderDto.ReadFolderRes> read(Long userId) {
		PageRequest pageRequest = new PageRequest(pagingConfig.getFolderConfig());

		return folderRepository.findByUserIdAndDeletedFalseOrDeletedNull(
			userId, pageRequest.of(Sort.Direction.ASC, "createdAt"))
			.stream()
			.map(FolderDto.ReadFolderRes::toDto)
			.collect(Collectors.toList());
	}

	@Transactional
	public FolderDto.ReadFolderRes update(FolderDto.UpdateFolderReq body) {
		Folder updatedFolder = updateFolder(body);
		return FolderDto.ReadFolderRes.toDto(updatedFolder);
	}

	private Folder updateFolder(FolderDto.UpdateFolderReq dto) {
		final Folder folder = folderRepository.findById(dto.getId()).get();
		folder.update(dto);
		return folder;
	}
}
