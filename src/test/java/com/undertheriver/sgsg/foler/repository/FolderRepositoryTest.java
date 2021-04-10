package com.undertheriver.sgsg.foler.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.foler.domain.dto.FolderDto;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@SpringBootTest
class FolderRepositoryTest {
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private UserRepository userRepository;

	Folder undeletedFolder;
	Folder deletedFolder;

	private static final String TEST_TITLE_VALUE = "테스트 폴더";
	private static final String TEST_TITLE_VALUE2 = "테스트 폴더2";
	private static final String TEST_TITLE_VALUE3 = "업데이트 테스트 폴더";

	private static final Integer TEST_POSITION_VALUE = 0;
	private static final Integer TEST_POSITION_VALUE2 = 1;

	private static final Integer NUMBER_OF_UNDELETED_FOLDERS = 1;
	private static final Integer NUMBER_OF_FOLDERS = 2;

	@BeforeEach
	public void beforeEach() {
		String rawPassword = "1234";
		User user = new User(rawPassword);
		user = userRepository.save(user);

		final FolderDto.CreateFolderReq req = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE)
			.color(FolderColor.BLACK)
			.position(TEST_POSITION_VALUE)
			.build();
		undeletedFolder = req.toEntity();

		final FolderDto.CreateFolderReq req2 = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE2)
			.color(FolderColor.BLACK)
			.position(TEST_POSITION_VALUE2)
			.build();
		deletedFolder = req2.toEntity();
		deletedFolder.setDeleted(true);

		undeletedFolder = folderRepository.save(undeletedFolder);
		deletedFolder = folderRepository.save(deletedFolder);
	}

	@DisplayName("Folder를 조회할 수 있다.")
	@Test
	@Order(0)
	public void read() {
		System.out.println("READ 먼저");
		List<Folder> folders = folderRepository.findAll();
		List<Folder> undeletedFolders = folderRepository.findAllByDeletedIsOrDeletedIs(false, null);

		System.out.println();
		assertAll(
			() -> assertThat(folders.size()).isEqualTo(NUMBER_OF_FOLDERS),
			() -> assertThat(undeletedFolders.size()).isEqualTo(NUMBER_OF_UNDELETED_FOLDERS)
		);
	}

	@DisplayName("Folder를 생성할 수 있다.")
	@Test
	@Order(1)
	public void save() {
		System.out.println("SAVE 먼저");
		assertAll(
			() -> assertThat(undeletedFolder.getTitle()).isEqualTo(TEST_TITLE_VALUE)
		);
	}

	@DisplayName("Folder 제목을 수정할 수 있다.")
	@Test
	@Order(2)
	public void updateTitle() {
		System.out.println("UPDATE 먼저");
		final FolderDto.UpdateFolderTitleReq req = FolderDto.UpdateFolderTitleReq.builder()
			.id(undeletedFolder.getId())
			.title(TEST_TITLE_VALUE3)
			.build();

		Folder beforeFolder = folderRepository.findById(req.getId()).get();
		String beforeTitle = beforeFolder.getTitle();
		Folder afterFolder = updateFolderTitle(beforeFolder.getId(), req);
		String afterTitle = afterFolder.getTitle();

		assertAll(
			() -> assertThat(beforeTitle).isNotEqualTo(afterTitle)
		);
	}

	// TODO: MOVE TO FolderService
	public Folder updateFolderTitle(Long id, FolderDto.UpdateFolderTitleReq dto) {
		final Folder folder = folderRepository.findById(id).get();
		folder.updateTitle(dto);
		return folder;
	}
}