package com.undertheriver.sgsg.foler.service;

import java.util.List;
import java.util.stream.Collectors;

import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.dto.PageRequest;
import com.undertheriver.sgsg.config.PagingConfig;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FolderService {
	private final FolderRepository folderRepository;
	private final UserService userService;
	private final PagingConfig pagingConfig;

	@Transactional
	public Long save(Long userId, FolderDto.CreateFolderReq req) {
		Integer limit = pagingConfig.getFolderConfig().get("limit");

		if (isFoldersExistsMoreThan20(userId, limit)) {
			throw new IndexOutOfBoundsException(
				String.format("폴더는 최대 %d개까지 생성할 수 있습니다!", limit));
		}

		Folder folder = folderRepository.save(req.toEntity());
		User loginUser = userService.findById(userId);
		folder.setUser(loginUser);
		loginUser.addFolder(folder);
		return folder.getId();
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
	public FolderDto.ReadFolderRes update(Long folderId, FolderDto.UpdateFolderReq dto) {
		final Folder folder = folderRepository.findById(folderId).get();
		folder.update(dto);
		return FolderDto.ReadFolderRes.toDto(folder);
	}

	@Transactional(readOnly = true)
	public FolderDto.GetNextFolderColorRes getNextColor(Long userId) {
		Integer folderCount = folderRepository.countByUserId(userId);
		FolderColor nextColor = FolderColor.getNextColor(folderCount);
		return FolderDto.GetNextFolderColorRes.builder()
			.nextColor(nextColor)
			.build();
	}
}
