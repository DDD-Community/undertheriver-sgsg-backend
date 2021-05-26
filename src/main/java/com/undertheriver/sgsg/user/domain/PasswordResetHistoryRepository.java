package com.undertheriver.sgsg.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetHistoryRepository extends JpaRepository<PasswordResetHistory, Long> {

    Optional<PasswordResetHistory> findByUserIdAndInitializationCredential(Long userId,
        String initializationCredential);
}
