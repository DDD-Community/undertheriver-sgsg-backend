package com.undertheriver.sgsg.user.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "deleted IS NULL")
@Table(indexes = {
    @Index(name = "password_reset_history_idx_initialization_credential", columnList = "initializationCredential"),
    @Index(name = "password_reset_history_idx_user_id", columnList = "userId")
})
public class PasswordResetHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String initializationCredential;

    private LocalDateTime expiredAt;

    @Builder
    protected PasswordResetHistory(Long userId, String initializationCredential, LocalDateTime expiredAt) {
        this.userId = userId;
        this.initializationCredential = initializationCredential;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired(LocalDateTime currentTime) {
        return currentTime.isAfter(this.expiredAt);
    }
}
