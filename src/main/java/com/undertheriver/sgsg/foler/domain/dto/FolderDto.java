package com.undertheriver.sgsg.foler.domain.dto;

import com.nimbusds.oauth2.sdk.auth.Secret;
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

        @Builder
        public CreateFolderReq(String title, FolderColor color) {
            this.title = title;
            this.color = color;
        }

        public Folder toEntity() {
            return Folder.builder()
                .title(this.title)
                .color(this.color)
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

        @Builder
        public ReadFolderRes(Long id, String title, FolderColor color) {
            this.id = id;
            this.title = title;
            this.color = color;
        }

        public static ReadFolderRes toDto(Folder folder) {
            return ReadFolderRes.builder()
                .id(folder.getId())
                .title(folder.getTitle())
                .color(folder.getColor())
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
