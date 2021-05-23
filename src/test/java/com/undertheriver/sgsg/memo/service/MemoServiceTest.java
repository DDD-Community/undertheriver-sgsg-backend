package com.undertheriver.sgsg.memo.service;

import static com.undertheriver.sgsg.common.exception.BadRequestException.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.undertheriver.sgsg.common.exception.BadRequestException;
import com.undertheriver.sgsg.common.exception.ModelNotFoundException;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@Transactional
@SpringBootTest
class MemoServiceTest {
    private static final String FOLDER_TITLE_TEST = "메모입니다 메모";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private MemoService memoService;
    @Autowired
    private MemoRepository memoRepository;

    private User createUser() {
        return User.builder()
            .name("황성찬")
            .userRole(UserRole.USER)
            .profileImageUrl("http://naver.com/test.png")
            .email("dbfpzk142@gmail.com")
            .build();
    }

    private Folder createFolder() {
        return Folder.builder()
            .title("폴더 테스트")
            .color(FolderColor.BLUE)
            .build();
    }

    private Memo createMemo() {
        return Memo.builder()
            .content("테스트 메모")
            .build();
    }

    @DisplayName("폴더가 있을 때 메모를 생성할 수 있다.")
    @Test
    public void create() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder folder = createFolder();
        folderRepository.save(folder);

        MemoDto.CreateMemoReq request = MemoDto.CreateMemoReq.builder()
            .folderId(folder.getId())
            .folderTitle(folder.getTitle())
            .folderColor(folder.getColor())
            .memoContent(FOLDER_TITLE_TEST)
            .build();

        // when
        Long actualMemoId = memoService.save(user.getId(), request);

        // then
        assertNotNull(actualMemoId);
    }

    @DisplayName("폴더가 없을 때 폴더를 먼저 생성 후 메모를 생성할 수 있다.")
    @Test
    public void createEvenIfNoFolder() {
        // given
        User user = createUser();
        userRepository.save(user);

        MemoDto.CreateMemoReq noFolderRequest = MemoDto.CreateMemoReq.builder()
            .folderTitle("테스트 폴더")
            .folderColor(FolderColor.BLUE)
            .memoContent("테스트 메모")
            .build();

        // when
        Long actualMemoId = memoService.save(user.getId(), noFolderRequest);

        // then
        Memo actualMemo = memoRepository.findById(actualMemoId)
            .orElseThrow(ModelNotFoundException::new);

        Long actualFolderId = actualMemo.getFolder().getId();
        Long expectedFolderId = folderService.read(actualFolderId).getId();

        assertEquals(expectedFolderId, actualFolderId);
    }

    @DisplayName("메모를 수정할 수 있다.")
    @Test
    public void updateMemo() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder folder = createFolder();
        folderRepository.save(folder);

        Memo memo = createMemo();
        memoRepository.save(memo);
        folder.addMemo(memo);

        String expectedContent = "다나가";
        String expectedThumbnailUrl = "https://sgsg.site";
        Boolean expectedFavorite = true;
        MemoDto.UpdateMemoReq request = MemoDto.UpdateMemoReq.builder()
            .folderId(folder.getId())
            .content(expectedContent)
            .thumbnailUrl(expectedThumbnailUrl)
            .favorite(expectedFavorite)
            .build();

        // when
        memoService.update(memo.getId(), request);
        String actualContent = memo.getContent();
        String actualThumbnailUrl = memo.getThumbnailUrl();
        Boolean actualFavorite = memo.getFavorite();

        // then
        assertEquals(expectedContent, actualContent);
        assertEquals(expectedThumbnailUrl, actualThumbnailUrl);
        assertEquals(expectedFavorite, actualFavorite);
    }

    @DisplayName("메모 전부를 조회할 수 있다.")
    @Test
    public void readAllMemos() {
        // given
        int folderSize = 10;
        int memoSizePerFolder = 5;
        int expectedMemoSize = folderSize * memoSizePerFolder;
        User user = createUser();
        userRepository.save(user);
        givenReadAllApi(user, folderSize, memoSizePerFolder);

        // when
        List<MemoDto.ReadMemoRes> actualMemos = memoService.readAll(user.getId(), null);

        // then
        int actualMemoSize = actualMemos.size();
        assertEquals(expectedMemoSize, actualMemoSize);
    }

    @DisplayName("폴더 별 메모를 조회할 수 있다.")
    @Test
    public void readAllMemosByFolder() {
        // given
        int folderSize = 10;
        int memoSizePerFolder = 5;
        int expectedMemoSize = folderSize * memoSizePerFolder;
        User user = createUser();
        userRepository.save(user);
        List<Folder> folders = givenReadAllApi(user, folderSize, memoSizePerFolder);
        Long folderId = folders.get(0).getId();

        // when
        List<MemoDto.ReadMemoRes> actualMemos = memoService.readAll(user.getId(), folderId);

        // then
        int actualMemoSize = actualMemos.size();
        assertEquals(expectedMemoSize / folderSize, actualMemoSize);
    }

    private List<Folder> givenReadAllApi(User user, int folderSize, int memoSizePerFolder) {
        List<Folder> folders = new ArrayList<>();
        for (int i = 0; i < folderSize; i++) {
            Folder folder = Folder.builder()
                .title("테스트")
                .user(user)
                .build();
            folders.add(folder);
        }
        folderRepository.saveAll(folders);

        List<Memo> memos = new ArrayList<>();
        for (int i = 0; i < folderSize / 2; i++) {
            for (int j = 0; j < memoSizePerFolder; j++) {
                Memo notFavoriteMemo = Memo.builder()
                    .folder(folders.get(i))
                    .content("메모 내용" + j)
                    .build();
                memos.add(notFavoriteMemo);
            }
        }

        for (int i = folderSize / 2; i < folderSize; i++) {
            for (int j = 0; j < memoSizePerFolder; j++) {
                Memo favoriteMemo = Memo.builder()
                    .content("메모 내용" + j)
                    .folder(folders.get(i))
                    .favorite(true)
                    .build();
                memos.add(favoriteMemo);
            }
        }
        memoRepository.saveAll(memos);

        return folders;
    }

    @DisplayName("메모를 삭제할 수 있다.")
    @Test
    public void delete() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder folder = createFolder();
        folderRepository.save(folder);
        user.addFolder(folder);

        Memo memo = createMemo();
        memoRepository.save(memo);
        folder.addMemo(memo);

        // when
        memoService.delete(memo.getId());

        // then
        assertTrue(memo.getDeleted());
    }

    @DisplayName("메모를 즐겨찾기할 수 있다.")
    @Test
    public void favorite1() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder folder = createFolder();
        folderRepository.save(folder);
        user.addFolder(folder);

        Memo memo = createMemo();
        memoRepository.save(memo);
        folder.addMemo(memo);

        // when
        memoService.favorite(user.getId(), memo.getId());

        // then
        assertTrue(memo.getFavorite());
    }

    @DisplayName("메모 즐겨찾기를 취소할 수 있다.")
    @Test
    public void favorite2() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder folder = createFolder();
        folderRepository.save(folder);
        user.addFolder(folder);

        Memo memo = createMemo();
        memoRepository.save(memo);
        folder.addMemo(memo);

        // when
        memoService.unfavorite(user.getId(), memo.getId());

        // then
        assertFalse(memo.getFavorite());
    }

    @DisplayName("사용자의 메모가 아니면 즐겨찾기를 취소할 수 없다.")
    @Test
    public void favorite3() {
        // given
        User user = createUser();
        User user2 = createUser();
        userRepository.saveAll(Arrays.asList(user, user2));

        Folder folder = createFolder();
        folderRepository.save(folder);
        user.addFolder(folder);

        Memo memo = createMemo();
        memoRepository.save(memo);
        folder.addMemo(memo);

        Long wrongUserId = user2.getId();

        // when
        BadRequestException thrown = assertThrows(
            BadRequestException.class,
            () -> memoService.unfavorite(wrongUserId, memo.getId())
        );

        // then
        assertEquals(thrown.getMessage(), UNMACHED_USER);
    }

    @DisplayName("비밀 폴더에 속한 메모들은 메모 내용을 숨길 수 있다.")
    @Test
    public void readAllMemosByFolder2() {
        // given
        User user = createUser();
        userRepository.save(user);

        Folder secretFolder = createFolder();
        secretFolder.secret();
        folderRepository.save(secretFolder);
        user.addFolder(secretFolder);

        Memo memo = createMemo();
        memoRepository.save(memo);

        String expectedMemoContent = "";

        // when
        boolean expectedTrue = memoService.readAll(user.getId(), null)
            .stream()
            .filter(MemoDto.ReadMemoRes::getSecret)
            .map(MemoDto.ReadMemoRes::getMemoContent)
            .allMatch(memoContent -> memoContent.equals(expectedMemoContent));

        // then
        assertTrue(expectedTrue);
    }
}