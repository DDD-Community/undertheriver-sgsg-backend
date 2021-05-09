package com.undertheriver.sgsg.memo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(indexes = @Index(name = "fk_folder", columnList = "folder"))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue
    private long id;

    @Lob
    private String content;

    private Boolean favorite;

    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "folder")
    private Folder folder;

    @Builder
    public Memo(String content, Boolean favorite, String thumbnailUrl, Folder folder) {
        this.content = content;
        this.favorite = favorite;
        this.thumbnailUrl = thumbnailUrl;
        this.folder = folder;
    }

    public void mapFolder(Folder folder) {
        this.folder = folder;
    }

    public void update(MemoDto.UpdateMemoReq dto, Folder folder) {
        this.content = dto.getContent();
        this.favorite = dto.getFavorite();
        this.thumbnailUrl = dto.getThumbnailUrl();
        this.mapFolder(folder);
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
