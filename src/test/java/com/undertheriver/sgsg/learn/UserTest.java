package com.undertheriver.sgsg.learn;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@SpringBootTest
class UserTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    @DisplayName("연관관계 소프트딜리트 쿼리확인 테스트")
    void name() {

        User user = User.builder()
            .email("test@test.com")
            .profileImageUrl("http://naver.com/adf.png")
            .userRole(UserRole.USER)
            .name("TEST")
            .build();

        User savedUser = userRepository.save(user);
        savedUser.saveApiClient("testID", "GOOGLE");

        entityManager.flush();
        entityManager.clear();

        User persistedUser = userRepository.findById(savedUser.getId()).get();
        System.out.println(persistedUser.getUserApiClients().size());
    }
}