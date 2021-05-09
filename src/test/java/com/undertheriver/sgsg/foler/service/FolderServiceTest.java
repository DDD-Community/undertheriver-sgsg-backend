package com.undertheriver.sgsg.foler.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.undertheriver.sgsg.foler.domain.FolderOrderBy;
import com.undertheriver.sgsg.foler.repository.FolderRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.repository.MemoRepository;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;
import org.springframework.transaction.annotation.Transactional;

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

    private FolderDto.CreateFolderReq createFolderReq;
    private FolderDto.CreateFolderReq createFolderReq2;
    private FolderDto.UpdateFolderTitleReq updateFolderTitleReq;
    private User user;

    private static final String TEST_TITLE_VALUE1 = "나";
    private static final String TEST_TITLE_VALUE2 = "가";

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .name("김홍빈")
                .userRole(UserRole.USER)
                .profileImageUrl("http://naver.com/test.png")
                .userSecretMemoPassword("1234")
                .email("fusis1@naver.com")
                .build();
        userRepository.save(user);

        createFolderReq = FolderDto.CreateFolderReq.builder()
            .title(TEST_TITLE_VALUE1)
            .color(FolderColor.RED)
            .build();

        createFolderReq2 = FolderDto.CreateFolderReq.builder()
            .title(TEST_TITLE_VALUE2)
            .color(FolderColor.RED)
            .build();
    }

    @DisplayName("Folder를 메모 많은 순으로 조회할 수 있다.")
    @Test
    public void readOrderByMemos() {
        // given
        Folder expectedFolder = folderRepository.save(createFolderReq.toEntity());
        Folder notExpectedFolder = folderRepository.save(createFolderReq2.toEntity());
        user.addFolder(expectedFolder);
        user.addFolder(notExpectedFolder);

        Memo memo1 = Memo.builder()
            .content("메모1")
            .build();
        Memo memo2 = Memo.builder()
            .content("메모2")
            .build();
        Memo memo3 = Memo.builder()
            .content("메모3")
            .build();
        memo1 = memoRepository.save(memo1);
        memo2 = memoRepository.save(memo2);
        memo3 = memoRepository.save(memo3);
        expectedFolder.addMemo(memo1);
        expectedFolder.addMemo(memo2);
        notExpectedFolder.addMemo(memo3);
        Long expectedFolderId = expectedFolder.getId();

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.MEMO);
        Long actualFolderId = actualFolderList.get(0).getId();


        // then
        assertTrue(expectedFolderId.equals(actualFolderId));
    }

    @DisplayName("Folder를 폴더 이름 순 으로 조회할 수 있다.")
    @Test
    public void readOrderByName() {
        // given
        Folder expectedFolder = folderRepository.save(createFolderReq.toEntity());
        Folder notExpectedFolder = folderRepository.save(createFolderReq2.toEntity());
        user.addFolder(expectedFolder);
        user.addFolder(notExpectedFolder);
        String expectedFolderTitle = TEST_TITLE_VALUE2;

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.NAME);
        String actualFolderTitle = actualFolderList.get(0).getTitle();

        // then
        assertTrue(expectedFolderTitle.equals(actualFolderTitle));
    }

    @DisplayName("Folder를 폴더 생성 순 으로 조회할 수 있다.")
    @Test
    public void readOrderByCreated() {
        // given
        Folder expectedFolder = folderRepository.save(createFolderReq.toEntity());
        Folder notExpectedFolder = folderRepository.save(createFolderReq2.toEntity());
        user.addFolder(expectedFolder);
        user.addFolder(notExpectedFolder);
        Long expectedFolderId = expectedFolder.getId();

        // when
        List<FolderDto.ReadFolderRes> actualFolderList = folderService.readAll(user.getId(), FolderOrderBy.CREATED_AT);
        Long actualFolderId = actualFolderList.get(0).getId();

        // then
        assertTrue(expectedFolderId.equals(actualFolderId));
    }

    @DisplayName("Folder를 20개 이하일 때만 저장할 수 있다.")
    @Test
    public void save() {
        Long id = folderService.save(user.getId(), createFolderReq);
        assertAll(
                () -> assertThat(id).isNotEqualTo(null)
        );
    }

    @DisplayName("Folder를 20개 이상일 때 저장할 수 없다.")
    @Test
    public void saveFailed() {
        // given
        List<Folder> folderList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            folderList.add(
                    FolderDto.CreateFolderReq.builder()
                            .title(i + "")
                            .color(FolderColor.RED)
                            .build()
                            .toEntity()
            );
        }
        folderRepository.saveAll(folderList);
        folderList.forEach(f -> user.addFolder(f));

        // when, then
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            folderService.save(user.getId(), createFolderReq);
        });
    }

    @DisplayName("Folder를 수정할 수 있다")
    @Test
    public void updateFolder() {
        // given
        Folder folder = folderRepository.save(createFolderReq.toEntity());
        user.addFolder(folder);
        String defaultTitle = folder.getTitle();
        String expectedTitle = TEST_TITLE_VALUE2;
        updateFolderTitleReq = FolderDto.UpdateFolderTitleReq.builder()
                .title(expectedTitle)
                .build();

        // when
        String actualTitle = folderService.update(folder.getId(), updateFolderTitleReq)
                .getTitle();

        // then
        assertEquals(expectedTitle, actualTitle);
        assertNotEquals(defaultTitle, actualTitle);
    }

    @DisplayName("다음 생성할 폴더 색상을 알려준다.")
    @Test
    public void nextColorTestCase() {
        // given
        FolderColor[] colors = FolderColor.values();
        FolderColor expectedColor = FolderColor.values()[0];
        int multipleOfFolderColorLength = colors.length;

        // when
        FolderColor actualColor = FolderColor.getNextColor(multipleOfFolderColorLength);

        // then
        assertEquals(actualColor, expectedColor);
    }
}