package com.undertheriver.sgsg.memo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {
	private final MemoRepository memoRepository;
	private final FolderService folderService;

	@Transactional
	public Long save(Long userId, MemoDto.CreateMemoReq body) {
		
		if (body.getFolderId() == null) {
			String folderTitle = body.getFolderTitle();
			FolderColor nextColor = folderService.getNextColor(userId).getNextColor();

			FolderDto.CreateFolderReq req = FolderDto.CreateFolderReq.builder()
				.title(folderTitle)
				.color(nextColor)
				.build();

			Long folderId = folderService.save(userId, req).getId();
			body.setFolderId(folderId);
		}

		Folder folder = folderService.read(body.getFolderId()).get();
		Memo memo = Memo.builder()
			.content(body.getMemoContent())
			.folder(Folder.builder().)
			.build();
		memo = memoRepository.save(memo);
		return memo.getId();
	}
}
