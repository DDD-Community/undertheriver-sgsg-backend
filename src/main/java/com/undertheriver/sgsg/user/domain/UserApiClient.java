package com.undertheriver.sgsg.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.undertheriver.sgsg.common.domain.BaseEntity;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
    @Index(name = "user_api_clients_idx_user", columnList = "user_id"),
    @Index(name = "user_api_clients_idx_oauth_id", columnList = "oauth_id")}
)
@NoArgsConstructor
@Where(clause = "deleted IS NULL")
public class UserApiClient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private String oAuthId;

    @Enumerated(EnumType.STRING)
    private OAuthName oAuthName;

    @ManyToOne
    private User user;

    public UserApiClient(String oAuthId, String oAuthName, User user) {
        this.oAuthId = oAuthId;
        this.oAuthName = OAuthName.of(oAuthName);
        this.user = user;
    }

    public boolean isSameOAuthName(String oAuthName) {
        return this.oAuthName.isSame(oAuthName);
    }
}
