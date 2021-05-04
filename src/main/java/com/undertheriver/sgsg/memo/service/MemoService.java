package com.undertheriver.sgsg.memo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {
	private final MemoRepository memoRepository;
	private final FolderRepository folderRepository;
	private final UserRepository userRepository;

	@Transactional
	public Memo save(Long userId, MemoDto.CreateMemoReq body) {
		Folder folder = createOrReadFolder(body);
		Memo memo = memoRepository.save(body.toMemoEntity());
		folder.addMemo(memo);
		return memo;
	}

	private Folder createOrReadFolder(MemoDto.CreateMemoReq body) {
		if (body.hasFolderId()) {
			Long folderId = body.getFolderId();
			return folderRepository.findById(folderId)
				.orElseThrow(ModelNotFoundException::new);
		}

		return folderRepository.save(body.toFolderEntity());
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
