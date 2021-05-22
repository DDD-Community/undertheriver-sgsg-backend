package com.undertheriver.sgsg.foler.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(indexes = @Index(name = "folder_idx_user", columnList = "user"))
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FolderColor color;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(mappedBy = "folder")
    private Set<Memo> memos = new LinkedHashSet<>();
    
    private Boolean secret;

    public boolean isSecret() {
        if (secret == null) {
            return false;
        }

        return secret;
    }

    @Builder
    public Folder(String title, FolderColor color, User user) {
        this.title = title;
        this.color = color;
        this.user = user;
    }

    public void update(FolderDto.UpdateFolderTitleReq dto) {
        this.title = dto.getTitle();
    }

    public void addMemo(Memo memo) {
        this.memos.add(memo);
        memo.mapFolder(this);
    }

    public void mapUser(User user) {
        this.user = user;
    }

    public void secret() {
        secret = true;
    }

    public void unsecret() {
        secret = false;
    }

    public boolean hasBy(Long userId) {
        return user.getId().equals(userId);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Folder)) {
            return false;
        }

        final Folder folder = (Folder)other;
        Long otherId = folder.getId();

        return otherId.equals(this.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}


