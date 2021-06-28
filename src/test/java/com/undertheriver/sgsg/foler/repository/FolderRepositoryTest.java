package com.undertheriver.sgsg.foler.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@Transactional
@SpringBootTest
public class FolderRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @DisplayName("유저가 가진 폴더 중 제목이 일치하는 가장 첫 번째 폴더를 조회할 수 있다.")
    @Test
    public void test() {
        // given
        User user = User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .build();
        userRepository.save(user);

        String expected = "";
        Folder folder = Folder.builder()
            .title(expected)
            .build();
        folderRepository.save(folder);
        user.addFolder(folder);

        // when
        String actual = folderRepository.findFirstByUserIdAndTitle(user.getId(), folder.getTitle())
            .get().getTitle();

        // then
        assertEquals(expected, actual);
    }
}
