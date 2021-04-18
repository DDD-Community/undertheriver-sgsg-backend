package com.undertheriver.sgsg.foler.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.foler.service.FolderService;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@SpringBootTest
class FolderRepositoryTest {
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FolderService folderService;

	FolderDto.CreateFolderReq createFolderReq1;
	FolderDto.CreateFolderReq createFolderReq2;
	FolderDto.UpdateFolderReq updateFolderReq1;
	FolderDto.UpdateFolderReq updateFolderReq2;
	User user;

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

		createFolderReq1 = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE1)
			.color(FolderColor.BLACK)
			.build();

		createFolderReq2 = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE2)
			.color(FolderColor.WHITE)
			.build();
	}

	@Disabled
	@DisplayName("Folder를 조회할 수 있다.")
	@Test
	@Disabled
	public void read() {
		folderRepository.save(createFolderReq1.toEntity());
		List<Folder> folder = folderRepository.findFirst20ByUserAndDeletedFalseOrDeletedNull(user);
		assertAll(
			() -> assertThat(folder.size()).isGreaterThan(0)
		);
	}

	@DisplayName("Folder를 20개 이하일 때만 저장할 수 있다.")
	@Test
	public void save() {
		Long id = folderService.save(createFolderReq1);
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
					.user(user)
					.title(i + "")
					.color(FolderColor.BLACK)
					.build()
					.toEntity()
			);
		}

		folderRepository.saveAll(folderList);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			folderService.save(createFolderReq1);
		});
	}

	@DisplayName("Folder를 수정할 수 있다")
	@Test
	public void updateFolder() {
		Folder beforeFolder1 = folderRepository.save(createFolderReq1.toEntity());
		Folder beforeFolder2 = folderRepository.save(createFolderReq2.toEntity());

		updateFolderReq1 = FolderDto.UpdateFolderReq.builder()
			.id(beforeFolder1.getId())
			.title(beforeFolder2.getTitle())
			.color(beforeFolder2.getColor())
			.build();

		updateFolderReq2 = FolderDto.UpdateFolderReq.builder()
			.id(beforeFolder2.getId())
			.title(beforeFolder1.getTitle())
			.color(beforeFolder1.getColor())
			.build();

		List<FolderDto.UpdateFolderReq> req = new ArrayList<>();
		req.add(updateFolderReq1);
		req.add(updateFolderReq2);

		List<FolderDto.ReadFolderRes> res = folderService.update(req);


		FolderDto.ReadFolderRes afterFolder1 = res.get(0);
		FolderDto.ReadFolderRes afterFolder2 = res.get(1);


		assertAll(
			() -> assertThat(beforeFolder1.getTitle()).isNotEqualTo(afterFolder1.getTitle()),
			() -> assertThat(beforeFolder2.getTitle()).isNotEqualTo(afterFolder2.getTitle())
		);
	}

}


