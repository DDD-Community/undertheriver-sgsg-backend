package com.undertheriver.sgsg.foler.service;

import static com.undertheriver.sgsg.common.exception.BadRequestException.*;
import static com.undertheriver.sgsg.common.exception.FolderValidationException.*;
import static com.undertheriver.sgsg.user.exception.PasswordValidationException.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.common.exception.FolderValidationException;
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

@Transactional(readOnly = true)
@Service
public class FolderService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final Integer folderLimit;

    public FolderService(
        BCryptPasswordEncoder bCryptPasswordEncoder,
        FolderRepository folderRepository,
        UserRepository userRepository,
        AppProperties appProperties
    ) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.folderLimit = appProperties.getFolder().getLimit();
    }

    @Transactional
    public Long save(Long userId, FolderDto.CreateFolderReq req) {
        validateLimit(userId);
        validateDuplicate(userId, req.getTitle());

        Folder folder = folderRepository.save(req.toEntity());
        return folder.getId();
    }

    private void validateLimit(Long userId) {
        if (foldersExistMoreThanLimit(userId)) {
            throw new FolderValidationException(TOO_MANY_FOLDER);
        }
    }

    public void validateDuplicate(Long userId, String title) {
        boolean isDuplicated = folderRepository.findFirstByUserAndTitle(userId, title).isPresent();
        if (isDuplicated) {
            throw new FolderValidationException(DUPLICATE_FOLDER_NAME);
        }
    }

    private boolean foldersExistMoreThanLimit(Long userId) {
        Integer folderCount = folderRepository.countByUser(userId);
        return folderCount >= folderLimit;
    }

    public List<FolderDto.ReadFolderRes> readAll(Long userId, FolderOrderBy orderBy) {
        if (orderBy == null) {
            orderBy = FolderOrderBy.CREATED_AT;
        }

        return orderBy.findFolders(userId, folderRepository)
            .stream()
            .map(FolderDto.ReadFolderRes::toDto)
            .collect(Collectors.toList());
    }

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
        Integer folderCount = folderRepository.countByUser(userId);
        FolderColor nextColor = FolderColor.nextColorFrom(folderCount);
        return FolderDto.GetNextFolderColorRes.builder()
            .nextColor(nextColor)
            .build();
    }

    public void delete(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);
        folder.delete();
    }

    public void secret(Long userId, Long folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);

        validateUserHasFolder(userId, folder);

        folder.secret();
    }

    public void unsecret(Long userId, Long folderId, FolderDto.UnsecretReq request) {
        validateFolderPassword(userId, request.getPassword());

        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(ModelNotFoundException::new);

        validateUserHasFolder(userId, folder);

        folder.unsecret();
    }

    private void validateFolderPassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(ModelNotFoundException::new);

        if (!user.hasFolderPassword()) {
            throw new PasswordValidationException(NO_PASSWORD);
        }

        if (!bCryptPasswordEncoder.matches(rawPassword, user.getFolderPassword())) {
            throw new PasswordValidationException(PASSWORD_NOT_MATCH);
        }
    }

    private void validateUserHasFolder(Long userId, Folder folder) {
        if (!folder.hasBy(userId)) {
            throw new BadRequestException(UNMACHED_USER);
        }
    }
}
