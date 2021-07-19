package com.undertheriver.sgsg.foler.domain.dto;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FolderDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateFolderReq {
        private String title;
        private FolderColor color;
        private Long userId;

        @Builder
        public CreateFolderReq(String title, FolderColor color, Long userId) {
            this.title = title;
            this.color = color;
            this.userId = userId;
        }

        public Folder toEntity() {
            return Folder.builder()
                .title(this.title)
                .color(this.color)
                .userId(this.userId)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateFolderTitleReq {
        private String title;

        @Builder
        public UpdateFolderTitleReq(String title) {
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReadFolderRes {
        private Long id;
        private String title;
        private FolderColor color;
        private Integer memoCount;

        @Builder
        public ReadFolderRes(Long id, String title, FolderColor color, Integer memoCount) {
            this.id = id;
            this.title = title;
            this.color = color;
            this.memoCount = memoCount;
        }

        public static ReadFolderRes toDto(Folder folder) {
            return ReadFolderRes.builder()
                .id(folder.getId())
                .title(folder.getTitle())
                .color(folder.getColor())
                .memoCount(folder.getMemos().size())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetNextFolderColorRes {
        private FolderColor nextColor;

        @Builder
        public GetNextFolderColorRes(FolderColor nextColor) {
            this.nextColor = nextColor;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UnsecretReq {
        private String password;
    }
}
