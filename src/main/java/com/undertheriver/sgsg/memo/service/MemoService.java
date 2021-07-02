package com.undertheriver.sgsg.memo.service;

import static com.undertheriver.sgsg.common.exception.BadRequestException.*;
import static com.undertheriver.sgsg.common.exception.FolderValidationException.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.common.exception.FolderValidationException;
import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.foler.domain.Folder;
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
    private final FolderService folderService;

    @Transactional
    public Long save(Long userId, MemoDto.CreateMemoReq body) {
        Folder folder = createOrReadFolder(userId, body);
        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);
        user.addFolder(folder);
        Memo memo = memoRepository.save(body.toMemoEntity());
        folder.addMemo(memo);
        return memo.getId();
    }

    private Folder createOrReadFolder(Long userId, MemoDto.CreateMemoReq body) {
        if (body.hasFolderId()) {
            return getOneFolder(body.getFolderId());
        }

        folderService.validateDuplicate(userId, body.getFolderTitle());
        return folderRepository.save(body.toFolderEntity());
    }

    private Folder getOneFolder(Long folderId) {
        return folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
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

    public List<MemoDto.ReadMemoRes> readAll(Long userId, Long folderId) {
        if (Objects.isNull(folderId)) {
            return readAllByUser(userId);
        }
        return readAllByFolder(folderId);
    }

    private List<MemoDto.ReadMemoRes> readAllByUser(Long userId) {
        return memoRepository.findAllByUser(userId)
            .stream()
            .map(MemoDto.ReadMemoRes::toDto)
            .collect(Collectors.toList());
    }

    private List<MemoDto.ReadMemoRes> readAllByFolder(Long folderId) {
        return memoRepository.findAllByFolder(folderId)
            .stream()
            .map(MemoDto.ReadMemoRes::toDto)
            .collect(Collectors.toList());
    }

    public void delete(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(ModelNotFoundException::new);
        memo.delete();
    }

    public void favorite(Long userId, Long memoId) {
        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(ModelNotFoundException::new);

        validateUserHasMemo(userId, memo);

        memo.favorite();
    }

    public void unfavorite(Long userId, Long memoId) {
        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(ModelNotFoundException::new);

        validateUserHasMemo(userId, memo);

        memo.unfavorite();
    }

    private void validateUserHasMemo(Long userId, Memo memo) {
        if (memo.hasBy(userId)) {
            throw new BadRequestException(UNMACHED_USER);
        }
    }
}
