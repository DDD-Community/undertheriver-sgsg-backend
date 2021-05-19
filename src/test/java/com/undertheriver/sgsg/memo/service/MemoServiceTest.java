package com.undertheriver.sgsg.memo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

import org.springframework.transaction.annotation.Transactional;

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

    private User user;
    private Folder folder;
    private FolderDto.CreateFolderReq createFolderReq1;
    private MemoDto.CreateMemoReq createMemoReq1;
    private MemoDto.CreateMemoReq createMemoNoFolderReq1;

	@BeforeEach
	public void beforeEach() {
		user = User.builder()
			.name("김홍빈")
			.userRole(UserRole.USER)
			.profileImageUrl("http://naver.com/test.png")
			.email("fusis1@naver.com")
			.build();
		user = userRepository.save(user);

        createFolderReq1 = FolderDto.CreateFolderReq.builder()
            .title("폴더 테스트")
            .color(FolderColor.RED)
            .build();
    }

    @DisplayName("폴더가 있을 때 메모를 생성할 수 있다.")
    @Test
    public void create() {
        // given
        Long folderId = folderService.save(user.getId(), createFolderReq1);
        folder = folderRepository.findById(folderId).get();
        createMemoReq1 = MemoDto.CreateMemoReq.builder()
            .folderId(folder.getId())
            .folderTitle(folder.getTitle())
            .folderColor(folder.getColor())
            .memoContent(FOLDER_TITLE_TEST)
            .build();

        // when
        Long actualMemoId = memoService.save(user.getId(), createMemoReq1);

        // then
        assertTrue(
            folder.getMemos()
                .stream()
                .anyMatch(expectedMemo ->
                    expectedMemo.getId() == actualMemoId)
        );
    }

    @DisplayName("폴더가 없을 때 폴더를 먼저 생성 후 메모를 생성할 수 있다.")
    @Test
    public void createEvenIfNoFolder() {
        // given
        createMemoNoFolderReq1 = MemoDto.CreateMemoReq.builder()
            .folderTitle(createFolderReq1.getTitle())
            .folderColor(createFolderReq1.getColor())
            .memoContent(FOLDER_TITLE_TEST)
            .build();

        // when
        Long actualMemoId = memoService.save(user.getId(), createMemoNoFolderReq1);

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
        String expectedContent = "다나가";
        String expectedThumbnailUrl = "https://sgsg.site";
        Boolean expectedFavorite = true;
        Memo memo = Memo.builder()
            .content("안녕")
            .build();
        memo = memoRepository.save(memo);

        Long folderId = folderService.save(user.getId(), createFolderReq1);

        MemoDto.UpdateMemoReq req = MemoDto.UpdateMemoReq.builder()
            .folderId(folderId)
            .content(expectedContent)
            .thumbnailUrl(expectedThumbnailUrl)
            .favorite(expectedFavorite)
            .build();

        // when
        memoService.update(memo.getId(), req);
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
        givenReadMemos(folderSize, memoSizePerFolder);

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
        List<Folder> folders = givenReadMemos(folderSize, memoSizePerFolder);
        Long folderId = folders.get(0).getId();

        // when
        List<MemoDto.ReadMemoRes> actualMemos = memoService.readAll(user.getId(), folderId);

        // then
        int actualMemoSize = actualMemos.size();
        assertEquals(expectedMemoSize / folderSize, actualMemoSize);
    }

    private List<Folder> givenReadMemos(int folderSize, int memoSizePerFolder) {
        List<Folder> folders = new ArrayList<>();
        for (int i = 0; i < folderSize; i++) {
            Folder f = Folder.builder()
                .user(user)
                .title("폴더 제목" + i)
                .color(FolderColor.RED)
                .build();

            folders.add(f);
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
        Memo memo = Memo.builder()
            .content("테스트")
            .build();
        Memo persistedMemo = memoRepository.save(memo);

        // when
        memoService.delete(persistedMemo.getId());

        // then
        assertTrue(persistedMemo.getDeleted());
    }
}