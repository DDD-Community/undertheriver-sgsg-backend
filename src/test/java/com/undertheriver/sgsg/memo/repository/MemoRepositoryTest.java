package com.undertheriver.sgsg.memo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@Transactional
@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    MemoRepository memoRepository;

    @DisplayName("메모를 생성된 날짜 순으로 조회할 수 있다.")
    @Test
    public void test() {
        // given
        User user = User.builder()
            .name("김홍빈")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .build();
        userRepository.save(user);

        Folder folder = Folder.builder()
            .user(user)
            .title("안녕")
            .build();
        folderRepository.save(folder);

        Memo memo1 = Memo.builder()
            .folder(folder)
            .content("안녕1")
            .build();
        Memo expected = Memo.builder()
            .folder(folder)
            .content("안녕2")
            .build();
        memoRepository.save(memo1);
        memoRepository.save(expected);

        // when
        Memo actual = memoRepository.findAllByUser(user.getId()).get(0);

        // then
        assertEquals(expected, actual);
    }

}