package com.undertheriver.sgsg.memo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.memo.domain.Memo;
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
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
                .userSecretMemoPassword("1234")
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
        Long folderId = folderService.save(user.getId(), createFolderReq1).getId();
        folder = folderRepository.findById(folderId).get();
        createMemoReq1 = MemoDto.CreateMemoReq.builder()
                .folderId(folder.getId())
                .folderTitle(folder.getTitle())
                .folderColor(folder.getColor())
                .memoContent(FOLDER_TITLE_TEST)
                .build();

        // when
        Memo actualMemo = memoService.save(user.getId(), createMemoReq1);
        boolean expectedTrue = folder.getMemos()
                .stream()
                .anyMatch(expectedMemo ->
                        expectedMemo.equals(actualMemo));

        // then
        assertTrue(expectedTrue);
    }

    @DisplayName("폴더가 없을 때 메모를 생성할 수 있다.")
    @Test
    public void createEvenIfNoFolder() {
        // given
        createMemoNoFolderReq1 = MemoDto.CreateMemoReq.builder()
                .folderTitle(createFolderReq1.getTitle())
                .folderColor(createFolderReq1.getColor())
                .memoContent(FOLDER_TITLE_TEST)
                .build();

        // when
        Memo actualMemo = memoService.save(user.getId(), createMemoNoFolderReq1);
        Long actualFolderId = actualMemo.getFolder().getId();
        boolean expectedTrue = folderService.read(actualFolderId)
                .getId()
                .equals(actualFolderId);

        // then
        assertTrue(expectedTrue);
    }
}