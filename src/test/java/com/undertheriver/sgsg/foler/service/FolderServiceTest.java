package com.undertheriver.sgsg.foler.service;

import static com.undertheriver.sgsg.common.exception.BadRequestException.*;
import static com.undertheriver.sgsg.user.exception.PasswordValidationException.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.common.exception.FolderValidationException;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.FolderOrderBy;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import com.undertheriver.sgsg.user.domain.vo.UserSecretFolderPassword;
import com.undertheriver.sgsg.user.exception.PasswordValidationException;

@Transactional
@SpringBootTest
class FolderServiceTest {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemoRepository memoRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("Folder를 메모 많은 순으로 조회할 수 있다.")
    @Test
    public void readOrderByMemos() {
        // given
        User user = createUser("1234");
        userRepository.save(user);
        Long userId = user.getId();

        Folder moreMemoFolder = createFolder("테스트 폴더", userId);
        Folder lessMemoFolder = createFolder("테스트 폴더2", userId);
        folderRepository.saveAll(Arrays.asList(moreMemoFolder, lessMemoFolder));

        Memo memo1 = createMemo("메모1", null);
        Memo memo2 = createMemo("메모2", null);
        Memo memo3 = createMemo("메모3", null);
        memoRepository.saveAll(Arrays.asList(memo1, memo2, memo3));
        moreMemoFolder.addMemo(memo1);
        moreMemoFolder.addMemo(memo2);
        lessMemoFolder.addMemo(memo3);

        Long expectedFolderId = moreMemoFolder.getId();

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.MEMO);
        Long actualFolderId = actualFolderList.get(0).getId();

        // then
        assertEquals(expectedFolderId, actualFolderId);
    }

    @DisplayName("Folder를 폴더 이름 순 으로 조회할 수 있다.")
    @Test
    public void readOrderByName() {
        // given
        User user = createUser("1234");
        userRepository.save(user);
        Long userId = user.getId();

        Folder folder1 = createFolder("가나다", userId);
        Folder folder2 = createFolder("다나가", userId);
        folderRepository.saveAll(Arrays.asList(folder1, folder2));
        String expectedTitle = folder1.getTitle();

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.NAME);
        String actualFolderTitle = actualFolderList.get(0).getTitle();

        // then
        assertEquals(expectedTitle, actualFolderTitle);
    }

    @DisplayName("Folder를 폴더 생성 순 으로 조회할 수 있다.")
    @Test
    public void readOrderByCreated() {
        // given
        User user = createUser("1234");
        userRepository.save(user);
        Long userId = user.getId();

        Folder folder1 = createFolder("가나다", userId);
        Folder folder2 = createFolder("가나다", userId);
        folderRepository.saveAll(Arrays.asList(folder1, folder2));
        Long expectedFolderId = folder1.getId();

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.CREATED_AT);
        Long actualFolderId = actualFolderList.get(0).getId();

        // then
        assertEquals(expectedFolderId, actualFolderId);
    }

    @DisplayName("Folder 제한 개수 이상 생성할 수 없다.")
    @Test
    public void saveValidation1() {
        // given
        User user = createUser("1234");
        userRepository.save(user);
        Long userId = user.getId();

        List<Folder> folderList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            folderList.add(createFolder(String.format("테스트 폴더 %d", i), userId));
        }
        folderRepository.saveAll(folderList);

        FolderDto.CreateFolderReq createFolderReq = FolderDto.CreateFolderReq.builder()
            .title("테스트")
            .color(FolderColor.RED)
            .build();

        // when
        FolderValidationException thrown = assertThrows(
            FolderValidationException.class,
            () -> folderService.save(user.getId(), createFolderReq)
        );

        // then
        assertEquals(FolderValidationException.TOO_MANY_FOLDER, thrown.getMessage());
    }


    @DisplayName("Folder 이름은 중복될 수 없다.")
    @Test
    public void saveValidation2() {
        // given
        User user = createUser("1234");
        userRepository.save(user);

        String folderTitle = "중복";
        Folder folder = createFolder(folderTitle, user.getId());
        folderRepository.save(folder);

        FolderDto.CreateFolderReq createFolderReq = FolderDto.CreateFolderReq.builder()
            .title(folderTitle)
            .color(FolderColor.RED)
            .build();

        // when
        FolderValidationException thrown = assertThrows(
            FolderValidationException.class,
            () -> folderService.save(user.getId(), createFolderReq)
        );

        // then
        assertEquals(FolderValidationException.DUPLICATE_FOLDER_NAME, thrown.getMessage());
    }

    @DisplayName("Folder를 수정할 수 있다")
    @Test
    public void updateFolder() {
        // given
        Folder folder = createFolder("가나다", null);
        folderRepository.save(folder);

        FolderDto.UpdateFolderTitleReq req = FolderDto.UpdateFolderTitleReq.builder()
            .title("수정됨")
            .build();
        String expectedTitle = req.getTitle();

        // when
        String actualTitle = folderService.update(folder.getId(), req).getTitle();

        // then
        assertEquals(expectedTitle, actualTitle);
    }

    @DisplayName("폴더를 삭제할 수 있다.")
    @Test
    public void delete() {
        // given
        User user = createUser("1234");
        userRepository.save(user);

        Folder folder = createFolder("테스트", user.getId());
        folderRepository.save(folder);

        // when
        folderService.delete(folder.getId());

        // then
        assertTrue(folder.getDeleted());
    }

    @DisplayName("폴더를 비밀 상태로 만들 수 있다.")
    @Test
    public void secret() {
        //given
        User user = createUser("1234");
        userRepository.save(user);

        Folder folder = createFolder("테스트", user.getId());
        folderRepository.save(folder);

        // when
        folderService.secret(user.getId(), folder.getId());

        // then
        assertTrue(folder.isSecret());
    }

    @DisplayName("폴더를 비밀 상태를 취소할 있다.")
    @Test
    public void unsecret() {
        //given
        String rawPassword = "1234";
        User user = createUser(rawPassword);
        userRepository.save(user);

        Folder folder = createFolder("테스트 폴더", user.getId());
        folderRepository.save(folder);

        FolderDto.UnsecretReq request = new FolderDto.UnsecretReq(rawPassword);

        // when
        folderService.unsecret(user.getId(), folder.getId(), request);

        // then
        assertFalse(folder.isSecret());
    }

    @DisplayName("폴더 비밀번호를 설정하지 않았을 때는 비밀 상태를 취소할 없다.")
    @Test
    public void unsecret2() {
        //given
        User user = createNoPasswordUser();
        userRepository.save(user);

        Folder folder = createFolder("테스트", user.getId());
        folderRepository.save(folder);

        FolderDto.UnsecretReq request = new FolderDto.UnsecretReq("1234");

        // when
        PasswordValidationException thrown = assertThrows(
            PasswordValidationException.class,
            () -> folderService.unsecret(user.getId(), folder.getId(), request)
        );

        // then
        assertFalse(user.hasFolderPassword());
        assertEquals(NO_PASSWORD, thrown.getMessage());
    }

    @DisplayName("폴더 비밀번호가 일치하지 않으면 비밀 상태를 취소할 없다.")
    @Test
    public void unsecret3() {
        //given
        User user = createUser("1234");
        userRepository.save(user);

        Folder folder = createFolder("테스트", user.getId());
        folderRepository.save(folder);

        FolderDto.UnsecretReq request = new FolderDto.UnsecretReq("4321");

        // when
        PasswordValidationException thrown = assertThrows(
            PasswordValidationException.class,
            () -> folderService.unsecret(user.getId(), folder.getId(), request)
        );

        // then
        assertTrue(user.hasFolderPassword());
        assertEquals(PASSWORD_NOT_MATCH, thrown.getMessage());
    }

    @DisplayName("사용자의 폴더가 아니면 비밀폴더를 취소할 수 없다.")
    @Test
    public void unsecret4() {
        // given
        User user = createUser("1234");
        User user2 = createUser("1234");
        userRepository.saveAll(Arrays.asList(user, user2));

        Folder folder = createFolder("테스트", user.getId());
        folderRepository.save(folder);

        Long wrongUserId = user2.getId();

        FolderDto.UnsecretReq request = new FolderDto.UnsecretReq("1234");

        // when
        BadRequestException thrown = assertThrows(
            BadRequestException.class,
            () -> folderService.unsecret(wrongUserId, folder.getId(), request)
        );

        // then
        assertEquals(UNMACHED_USER, thrown.getMessage());
    }

    private User createUser(String rawPassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(rawPassword);
        UserSecretFolderPassword folderPassword = UserSecretFolderPassword.from(encryptedPassword);
        return User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .userSecretFolderPassword(folderPassword)
            .build();
    }

    private User createNoPasswordUser() {
        return User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("fusis1@naver.com")
            .userSecretFolderPassword(null)
            .build();
    }

    private Folder createFolder(String title, Long userId) {
        return Folder.builder()
            .title(title)
            .color(FolderColor.BLUE)
            .user(userId)
            .build();
    }

    private Memo createMemo(String content, Boolean favorite) {
        return Memo.builder()
            .content(content)
            .favorite(favorite)
            .build();
    }
}