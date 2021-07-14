package com.undertheriver.sgsg.memo.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(indexes = {
    @Index(name = "memo_idx_folder", columnList = "folder"),
    @Index(name = "memo_idx_folder_createdAt_favorite", columnList = "folder, createdAt, favorite")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted IS NULL")
public class Memo extends BaseEntity {

    private static final String EMPTY_STRING = "";
    private static final String MEMO_LIST_VIEW_TIME_PATTERN = "MM.dd";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String content;

    private Boolean favorite;

    private String thumbnailUrl;

    private String thumbnailFaviconUrl;

    private String thumbnailTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder")
    private Folder folder;

    @Builder
    protected Memo(
        String content, Boolean favorite, String thumbnailUrl, Folder folder, String thumbnailTitle,
        String thumbnailFaviconUrl
    ) {
        this.content = content;
        this.favorite = favorite;
        this.thumbnailUrl = thumbnailUrl;
        this.folder = folder;
        this.thumbnailTitle = thumbnailTitle;
        this.thumbnailFaviconUrl = thumbnailFaviconUrl;
    }

    public void mapFolder(Folder folder) {
        this.folder = folder;
    }

    public void update(MemoDto.UpdateMemoReq dto, Folder folder) {
        this.content = dto.getContent();
        this.favorite = dto.getFavorite();
        this.thumbnailUrl = dto.getThumbnailUrl();
        this.thumbnailTitle = dto.getThumbnailTitle();
        this.thumbnailFaviconUrl = dto.getThumbnailFaviconUrl();
        this.mapFolder(folder);
    }

    public void favorite() {
        favorite = true;
    }

    public void unfavorite() {
        favorite = false;
    }

    public boolean hasBy(Long userId) {
        return folder.hasBy(userId);
    }

    public String fetchContent() {
        if (folder.isSecret()) {
            return EMPTY_STRING;
        }
        return content;
    }

    public Long getFolderId() {
        return folder.getId();
    }

    public String getFolderTitle() {
        return folder.getTitle();
    }

    public FolderColor getFolderColor() {
        return folder.getColor();
    }

    public Boolean isSecret() {
        return folder.isSecret();
    }

    public String memoListViewDateTime() {
        LocalDateTime createdAt = super.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MEMO_LIST_VIEW_TIME_PATTERN);
        return createdAt.format(formatter);

    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Memo)) {
            return false;
        }

        final Memo memo = (Memo)other;
        Long otherId = memo.getId();

        return otherId.equals(this.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
