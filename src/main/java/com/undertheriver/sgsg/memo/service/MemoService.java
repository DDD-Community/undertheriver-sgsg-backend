package com.undertheriver.sgsg.memo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
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
    public Long save(Long userId, MemoDto.CreateMemoReq body) {
        Folder folder = createOrReadFolder(body);
        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);
        user.addFolder(folder);
        Memo memo = memoRepository.save(body.toMemoEntity());
        folder.addMemo(memo);
        return memo.getId();
    }

    private Folder createOrReadFolder(MemoDto.CreateMemoReq body) {
        if (body.hasFolderId()) {
            Long folderId = body.getFolderId();
            return folderRepository.findById(folderId)
                .orElseThrow(ModelNotFoundException::new);
        }

        return folderRepository.save(body.toFolderEntity());
    }

    @Transactional
    public MemoDto.UpdateMemoRes update(Long memoId, MemoDto.UpdateMemoReq body) {
        Long folderId = body.getFolderId();
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);

        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(ModelNotFoundException::new);

        memo.update(body, folder);
        return MemoDto.UpdateMemoRes.toDto(memo, folderId);
    }
}
