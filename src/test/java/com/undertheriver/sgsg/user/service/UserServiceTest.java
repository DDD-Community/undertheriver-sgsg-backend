package com.undertheriver.sgsg.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.controller.dto.FolderPasswordRequest;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("폴더 잠금 비밀번호를 설정할 수 있다.")
    @Test
    void name() {
        // given
        String rawPassword = "1234";
        User user = User.builder()
            .name("김홍빈")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .build();

        FolderPasswordRequest.CreateRequest request = new FolderPasswordRequest.CreateRequest(rawPassword);

        Long userId = repository.save(user).getId();
        User persistedUser = repository.findById(userId).get();


        // when
        service.createFolderPassword(user.getId(), request);
        String encryptedPassword = persistedUser.getFolderPassword();

        // then
        assertTrue(bCryptPasswordEncoder.matches(rawPassword, encryptedPassword));
    }
}
