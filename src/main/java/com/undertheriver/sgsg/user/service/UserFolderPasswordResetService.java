package com.undertheriver.sgsg.user.service;

import static java.time.LocalDateTime.*;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.infra.MailService;
import com.undertheriver.sgsg.user.domain.PasswordResetHistory;
import com.undertheriver.sgsg.user.domain.PasswordResetHistoryRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFolderPasswordResetService {

    private static final int PASSWORD_RESET_EXPIRED_TIME = 10;
    private static final String MAIL_TITLE = "비밀번호 초기화 안내";

    private final PasswordResetHistoryRepository passwordResetHistoryRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional
    public void requestInitializePassword(Long userId) {
        User user = getOne(userId);

        if (!user.hasFolderPassword()) {
            throw new BadRequestException("이미 패스워드가 없습니다.");
        }

        PasswordResetHistory history = PasswordResetHistory.builder()
            .userId(user.getId())
            .initializationCredential(UUID.randomUUID().toString())
            .expiredAt(now().plusMinutes(PASSWORD_RESET_EXPIRED_TIME))
            .build();

        passwordResetHistoryRepository.save(history);

        sendMail(user.getEmail(), history.getInitializationCredential());
    }

    @Transactional
    public void initializePassword(Long userId, String initializationCredential) {
        PasswordResetHistory passwordResetHistory = passwordResetHistoryRepository.findByUserIdAndInitializationCredential(
            userId, initializationCredential)
            .orElseThrow(ModelNotFoundException::new);

        if (passwordResetHistory.isExpired(now())) {
            throw new BadRequestException("인증 유효시간이 지났습니다");
        }

        User user = getOne(userId);
        user.initializePassword();
        passwordResetHistory.delete();
    }

    private void sendMail(String email, String initializationCredential) {
        mailService.send(MAIL_TITLE, email, Map.of("key", initializationCredential));
    }

    private User getOne(Long id) {
        return userRepository.findById(id)
            .orElseThrow(ModelNotFoundException::new);
    }
}
