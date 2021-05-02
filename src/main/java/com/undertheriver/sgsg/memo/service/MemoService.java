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
	public Memo save(Long userId, MemoDto.CreateMemoReq body) {
		Folder folder;

		if (body.getFolderId() == null) {
			FolderDto.CreateFolderReq req = getCreateFolderReq(body);
			folder = folderService.save(userId, req);
		} else {
			folder = folderService.read(body.getFolderId());
		}
		
		Memo memo = memoRepository.save(body.toEntity());
		memo.setFolder(folder);
		folder.addMemo(memo);
		return memo;
	}

	private FolderDto.CreateFolderReq getCreateFolderReq(MemoDto.CreateMemoReq body) {
		String folderTitle = body.getFolderTitle();
		FolderColor folderColor = body.getFolderColor();
		return FolderDto.CreateFolderReq.builder()
			.title(folderTitle)
			.color(folderColor)
			.build();
	}
}
