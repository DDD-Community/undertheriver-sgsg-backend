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
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.service.MemoService;
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
    @Autowired
    MemoService memoService;

    @DisplayName("메모를 생성된 날짜 순으로 조회할 수 있다.")
    @Test
    public void test() {
        // given
        User user = createUser();
        userRepository.save(user);
        Folder folder = createFolder(user);
        folderRepository.save(folder);
        Memo memo = createMemo(folder, "안녕", null);
        Memo expected = createMemo(folder, "안녕2", null);
        memoRepository.save(memo);
        memoRepository.save(expected);

        // when
        Memo actual = memoRepository.findAllByUser(user.getId()).get(0);

        // then
        assertEquals(expected, actual);
    }

    @DisplayName("메모를 즐겨찾기 된 순, 생성된 순으로 조회할 수 있다.")
    @Test
    public void test2() {
        // given
        User user = createUser();
        userRepository.save(user);
        Folder folder = createFolder(user);
        folderRepository.save(folder);
        Memo expected = createMemo(folder, "안녕", true);
        Memo memo = createMemo(folder, "안녕2", null);
        memoRepository.save(expected);
        memoRepository.save(memo);

        // when
        Memo actual = memoRepository.findAllByUser(user.getId()).get(0);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void 삭제된_폴더의_메모는_조회하지_않는다() {
        // given
        User user = createUser();
        userRepository.save(user);
        Memo memo = Memo.builder().build();
        memoRepository.save(memo);

        Folder folder = Folder.builder()
            .user(user.getId())
            .build();
        folder.addMemo(memo);
        folder.delete();
        folderRepository.save(folder);

        // when
        List<MemoDto.ReadMemoRes> actual = memoService.readAll(user.getId(), null);

        // then
        assertTrue(actual.size() == 0);

    }

    private User createUser() {
        return User.builder()
            .name("김홍빈")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .build();
    }

    private Folder createFolder(User user) {
        return Folder.builder()
            .user(user.getId())
            .title("안녕")
            .build();
    }

    private Memo createMemo(Folder folder, String content, Boolean favorite) {
        return Memo.builder()
            .folder(folder)
            .content(content)
            .favorite(favorite)
            .build();
    }
}