package com.undertheriver.sgsg.memo.domain.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.Memo;

import org.springframework.lang.Nullable;

import com.undertheriver.sgsg.foler.domain.FolderColor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemoDto {
    @Setter
    @Getter
    @NoArgsConstructor
    public static class CreateMemoReq {
        @Nullable
        private Long folderId;
        @NotNull
        private String folderTitle;
        @NotNull
        private FolderColor folderColor;
        @NotNull
        private String memoContent;

        @Builder
        public CreateMemoReq(Long folderId, String folderTitle, FolderColor folderColor, String memoContent) {
            this.folderId = folderId;
            this.folderTitle = folderTitle;
            this.folderColor = folderColor;
            this.memoContent = memoContent;
        }

        public Memo toMemoEntity() {
            return Memo.builder()
                .content(memoContent)
                .build();
        }

        public Folder toFolderEntity() {
            return Folder.builder()
                .title(folderTitle)
                .color(folderColor)
                .build();
        }

        public boolean hasFolderId() {
            return folderId != null;
        }
    }

    @Getter
    public static class UpdateMemoReq {
        @NotNull
        private String content;
        @Nullable
        private Boolean favorite;
        @Nullable
        private String thumbnailUrl;
        @NotNull
        private Long folderId;

        @Builder
        public UpdateMemoReq(
            String content, Boolean favorite, String thumbnailUrl, Long folderId) {
            this.content = content;
            this.favorite = favorite;
            this.thumbnailUrl = thumbnailUrl;
            this.folderId = folderId;
        }
    }

    @Getter
    public static class UpdateMemoRes {
        @NotNull
        private Long memoId;
        @NotNull
        private String content;
        @Nullable
        private Boolean favorite;
        @Nullable
        private String thumbnailUrl;
        @NotNull
        private Long folderId;
        @NotNull
        private LocalDate createdAt;

        @Builder
        public UpdateMemoRes(
            Long memoId, String content, Boolean favorite, String thumbnailUrl, Long folderId, LocalDate createdAt) {
            this.memoId = memoId;
            this.content = content;
            this.favorite = favorite;
            this.thumbnailUrl = thumbnailUrl;
            this.folderId = folderId;
            this.createdAt = createdAt;
        }

        public static UpdateMemoRes toDto(Memo memo, Long folderId) {
            return UpdateMemoRes.builder()
                .memoId(memo.getId())
                .content(memo.getContent())
                .favorite(memo.getFavorite())
                .thumbnailUrl(memo.getThumbnailUrl())
                .createdAt(memo.getCreatedAt())
                .folderId(folderId)
                .build();
        }
    }

    @Getter
    public static class ReadMemoRes {
        private Long memoId;
        private String memoContent;
        private LocalDate createdAt;
        private String thumbnailUrl;
        private Boolean favorite;
        private Long folderId;
        private String folderTitle;
        private FolderColor folderColor;
        private Boolean secret;

        @Builder
        public ReadMemoRes(Long memoId, String memoContent, LocalDate createdAt, String thumbnailUrl,
            Boolean favorite, Long folderId, String folderTitle, FolderColor folderColor, Boolean secret) {
            this.memoId = memoId;
            this.memoContent = memoContent;
            this.createdAt = createdAt;
            this.thumbnailUrl = thumbnailUrl;
            this.favorite = favorite;
            this.folderId = folderId;
            this.folderTitle = folderTitle;
            this.folderColor = folderColor;
            this.secret = secret;
        }

        public static ReadMemoRes toDto(Memo memo) {
            Folder folder = memo.getFolder();
            return ReadMemoRes.builder()
                .memoId(memo.getId())
                .memoContent(memo.fetchContent())
                .createdAt(memo.getCreatedAt())
                .thumbnailUrl(memo.getThumbnailUrl())
                .folderId(folder.getId())
                .folderTitle(folder.getTitle())
                .folderColor(folder.getColor())
                .favorite(memo.getFavorite())
                .secret(folder.isSecret())
                .build();
        }

        private static String setMemoContent(Memo memo) {
            Folder folder = memo.getFolder();
            if (folder.isSecret()) {
                return "";
            }

            return memo.getContent();
        }
    }

}
