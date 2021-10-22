package com.undertheriver.sgsg.foler.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@Transactional
@SpringBootTest
public class FolderRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MemoRepository memoRepository;

    @DisplayName("유저는 폴더 중 제목이 일치하는 가장 첫 번째 폴더를 조회할 수 있다.")
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
        Long userId = user.getId();

        String expected = "";
        Folder folder = Folder.builder()
            .title(expected)
            .user(userId)
            .build();
        folderRepository.save(folder);

        // when
        String actual = folderRepository.findFirstByUserAndTitle(userId, folder.getTitle())
            .get().getTitle();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void 메모가_있는_폴더를_메모순으로_조회할_수_있다() {
        // given
        Long userId = given();

        // when
        Comparator<Folder> comparator = (p1, p2) -> Integer.compare(p2.getMemos().size(), p1.getMemos().size());
        List<FolderDto.ReadFolderRes> actual = folderRepository.findAllByUser(userId, Sort.by(Sort.Direction.ASC, "createdAt"))
            .stream()
            .sorted(comparator)
            .map(FolderDto.ReadFolderRes::toDto)
            .collect(Collectors.toList());


        // then
        assertTrue(actual.get(0).getMemoCount() > actual.get(1).getMemoCount());
    }

    private Long given() {
        User user = User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .email("fusis1@naver.com")
            .build();
        userRepository.save(user);
        Long userId = user.getId();

        String empty = "";

        Memo memo = Memo.builder()
            .build();

        Memo memo2 = Memo.builder()
            .build();

        Memo memo3 = Memo.builder()
            .build();

        Folder folder = Folder.builder()
            .title(empty)
            .user(userId)
            .build();

        memoRepository.saveAll(Arrays.asList(memo, memo2, memo3));
        folder.addMemo(memo);
        folder.addMemo(memo2);
        folder.addMemo(memo3);
        folderRepository.save(folder);

        Memo memo4 = Memo.builder()
            .build();

        Folder folder2 = Folder.builder()
            .title(empty)
            .user(userId)
            .build();

        memoRepository.save(memo4);
        folder2.addMemo(memo4);
        folderRepository.save(folder2);
        return userId;
    }

    @Test
    public void 메모가_없는_폴더를_메모순으로_조회할_수_있다() {
        // given
        User user = User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .email("fusis1@naver.com")
            .build();
        userRepository.save(user);
        Long userId = user.getId();

        Folder folder = Folder.builder()
            .title("")
            .user(userId)
            .build();
        folderRepository.save(folder);

        // when
        Integer actual = folderRepository.findAllByUser(userId, Sort.by(Sort.Direction.ASC, "createdAt")).size();

        // then
        assertTrue(actual == 1);
    }
}
