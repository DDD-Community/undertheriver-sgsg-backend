package com.undertheriver.sgsg.foler.service;

import java.util.List;
import java.util.stream.Collectors;

import com.undertheriver.sgsg.foler.domain.FolderOrderBy;
import com.undertheriver.sgsg.user.domain.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.config.PagingConfig;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final PagingConfig pagingConfig;

    @Transactional
    public Long save(Long userId, FolderDto.CreateFolderReq req) {
        Integer limit = pagingConfig.getFolderConfig().get("limit");

        if (foldersExistMoreThanLimit(userId, limit)) {
            throw new IndexOutOfBoundsException(
                String.format("폴더는 최대 %d개까지 생성할 수 있습니다!", limit));
        }

        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);
        Folder folder = folderRepository.save(req.toEntity());
        user.addFolder(folder);
        return folder.getId();
    }

    private boolean foldersExistMoreThanLimit(Long userId, Integer limit) {
        Integer a = folderRepository.countByUserIdAndDeletedFalseOrDeletedNull(userId);
        return a >= limit;
    }

    @Transactional(readOnly = true)
    public List<FolderDto.ReadFolderRes> readAll(Long userId, FolderOrderBy orderBy) {
        return orderBy.findFolders(userId, folderRepository)
            .stream()
            .map(FolderDto.ReadFolderRes::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Folder read(Long folderId) {
        return folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
    }

    @Transactional
    public FolderDto.ReadFolderRes update(Long folderId, FolderDto.UpdateFolderTitleReq dto) {
        final Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
        folder.update(dto);
        return FolderDto.ReadFolderRes
            .toDto(folder);
    }

    @Transactional(readOnly = true)
    public FolderDto.GetNextFolderColorRes getNextColor(Long userId) {
        Integer folderCount = folderRepository.countByUserIdAndDeletedFalseOrDeletedNull(userId);
        FolderColor nextColor = FolderColor.getNextColor(folderCount);
        return FolderDto.GetNextFolderColorRes.builder()
            .nextColor(nextColor)
            .build();
    }
}
