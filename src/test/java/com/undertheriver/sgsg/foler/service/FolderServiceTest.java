package com.undertheriver.sgsg.foler.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.undertheriver.sgsg.foler.repository.FolderRepository;
import com.undertheriver.sgsg.memo.domain.Memo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.undertheriver.sgsg.common.dto.PageRequest;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.config.PagingConfig;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
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
    private FolderService folderService;
    @Autowired
    private PagingConfig pagingConfig;

    private FolderDto.CreateFolderReq createFolderReq;
    private FolderDto.UpdateFolderReq updateFolderReq;
    private User user;

    private static final String TEST_TITLE_VALUE1 = "테스트 폴더";
    private static final String TEST_TITLE_VALUE2 = "테스트 폴더2";

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
    }

    @DisplayName("Folder를 조회할 수 있다.")
    @Test
    public void read() {
        folderRepository.save(createFolderReq.toEntity());

        PageRequest pageRequest = new PageRequest(pagingConfig.getFolderConfig());
        List<Folder> folder = folderRepository.findByUserIdAndDeletedFalseOrDeletedNull(
                user.getId(), pageRequest.of(Sort.Direction.ASC, "createdAt"));

        assertAll(
                () -> assertThat(folder.size()).isGreaterThan(0)
        );
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
        updateFolderReq = FolderDto.UpdateFolderReq.builder()
                .title(expectedTitle)
                .build();

        // when
        String actualTitle = folderService.update(folder.getId(), updateFolderReq)
                .getTitle();

        // then
        assertEquals(expectedTitle, actualTitle);
        assertNotEquals(defaultTitle, actualTitle);
    }

    @DisplayName("다음 생성할 폴더 색상을 알려준다.")
    @Test
    public void nextColorTestCase1() {
        // given
        FolderColor expectedColor = FolderColor.values()[0];
        int notExistFolder = 0;

        // when
        FolderColor actualColor = FolderColor.getNextColor(notExistFolder);

        // then
        assertEquals(actualColor, expectedColor);
    }

    @DisplayName("다음 생성할 폴더 색상을 알려준다.")
    @Test
    public void nextColorTestCase2() {
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