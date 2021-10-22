package com.undertheriver.sgsg.foler.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import com.undertheriver.sgsg.foler.repository.FolderRepository;

public enum FolderOrderBy {
    NAME(Sort.by(Sort.Direction.ASC, "title")),
    CREATED_AT(Sort.by(Sort.Direction.ASC, "createdAt")),
    MEMO(Sort.by(Sort.Direction.ASC, "createdAt"));

    protected final Sort sort;

    FolderOrderBy(Sort sort) {
        this.sort = sort;
    }

    public List<Folder> findFolders(Long userId, FolderRepository folderRepository) {
        switch (this) {
            case NAME:
            case CREATED_AT:
                return folderRepository.findAllByUser(userId, sort);
            case MEMO:
                return folderRepository.findAllByUser(userId, sort)
                    .stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getMemos().size(), p1.getMemos().size()))
                    .collect(Collectors.toList());

            default:
                return Arrays.asList(new Folder());
        }
    }
}
