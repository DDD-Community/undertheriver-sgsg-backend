package com.undertheriver.sgsg.foler.service;

import static com.undertheriver.sgsg.user.exception.PasswordValidationException.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.config.AppProperties;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.FolderOrderBy;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import com.undertheriver.sgsg.user.exception.PasswordValidationException;

@Service
public class FolderService {
    private final FolderRepository folderRepository;

    private final UserRepository userRepository;
    private final Integer folderLimit;

    public FolderService(FolderRepository folderRepository,
        UserRepository userRepository, AppProperties appProperties) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.folderLimit = appProperties.getFolder().getLimit();
    }

    @Transactional
    public Long save(Long userId, FolderDto.CreateFolderReq req) {
        if (foldersExistMoreThanLimit(userId)) {
            throw new IndexOutOfBoundsException(
                String.format("폴더는 최대 %d개까지 생성할 수 있습니다!", folderLimit));
        }

        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);
        Folder folder = folderRepository.save(req.toEntity());
        user.addFolder(folder);
        return folder.getId();
    }

    private boolean foldersExistMoreThanLimit(Long userId) {
        Integer folderCount = folderRepository.countByUserIdAndDeletedFalseOrDeletedNull(userId);
        return folderCount >= folderLimit;
    }

    @Transactional(readOnly = true)
    public List<FolderDto.ReadFolderRes> readAll(Long userId, FolderOrderBy orderBy) {
        if (orderBy == null) {
            orderBy = FolderOrderBy.CREATED_AT;
        }

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

    public void delete(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
        folder.delete();
    }

    public FolderDto.SecretRes secret(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
        folder.secret();
        return FolderDto.SecretRes.toDto(folder);
    }

    public FolderDto.SecretRes unsecret(Long userId, Long folderId, FolderDto.UnsecretReq request) {
        validate(userId, request);
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
        folder.unsecret();
        return FolderDto.SecretRes.toDto(folder);
    }


    private void validate(Long userId, FolderDto.UnsecretReq request) {
        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);

        if (!user.hasFolderPassword()) {
            throw new PasswordValidationException(NO_PASSWORD);
        }

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getFolderPassword())) {
            throw new PasswordValidationException(PASSWORD_NOT_MATCH);
        }
    }
}
