package com.undertheriver.sgsg.foler.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@SpringBootTest
class FolderRepositoryTest {
	private static final String TEST_TITLE_VALUE1 = "테스트 폴더";
	private static final String TEST_TITLE_VALUE2 = "테스트 폴더2";
	private static final String TEST_TITLE_VALUE3 = "업데이트 테스트 폴더";
	private static final Integer TEST_POSITION_VALUE1 = 0;
	private static final Integer TEST_POSITION_VALUE2 = 1;
	private static final Integer NUMBER_OF_UNDELETED_FOLDERS = 1;
	private static final Integer NUMBER_OF_FOLDERS = 2;
	FolderDto.CreateFolderReq createFolderReq1;
	FolderDto.CreateFolderReq createFolderReq2;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void beforeEach() {
		String rawPassword = "1234";
		User user = User.builder()
			.userSecretMemoPassword("1234")
			.name("test")
			.email("hongbin@email.com")
			.profileImageUrl("http://naver.jpg")
			.userRole(UserRole.USER)
			.build();
		user = userRepository.save(user);

		createFolderReq1 = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE1)
			.color(FolderColor.BLACK)
			.position(TEST_POSITION_VALUE1)
			.build();

		createFolderReq2 = FolderDto.CreateFolderReq.builder()
			.user(user)
			.title(TEST_TITLE_VALUE2)
			.color(FolderColor.WHITE)
			.position(TEST_POSITION_VALUE2)
			.build();
	}

	@Disabled
	@DisplayName("Folder를 조회할 수 있다.")
	@Test
	@Disabled
	public void read() {
		Folder undeletedFolder = folderRepository.save(createFolderReq1.toEntity());
		Folder deletedFolder = folderRepository.save(createFolderReq2.toEntity());
		deletedFolder.setDeleted(true);

		List<Folder> folders = folderRepository.findAll();
		List<Folder> undeletedFolders = folderRepository.findAllByDeletedFalseOrDeletedNull();

		assertAll(
			() -> assertThat(folders.size()).isEqualTo(NUMBER_OF_FOLDERS),
			() -> assertThat(undeletedFolders.size()).isEqualTo(NUMBER_OF_UNDELETED_FOLDERS)
		);
	}

	@DisplayName("Folder를 20개 이하일 때 저장할 수 있다.")
	@Test
	public void save() {
		Folder folder = folderRepository.save(createFolderReq1.toEntity());
		List<Folder> folderCount = folderRepository.findFirst20ByUserAndDeletedFalseOrDeletedNull(folder.getUser());

		Folder savedFolder = Folder.builder().build();
		if (folderCount.size() <= 20) {
			savedFolder = folderRepository.save(createFolderReq2.toEntity());
		}

		String title = savedFolder.getTitle();
		assertAll(
			() -> assertThat(title).isEqualTo(TEST_TITLE_VALUE2)
		);
	}

	@DisplayName("Folder를 20개 이상일 때 저장할 수 없다.")
	@Test
	public void saveFailed() {

		String rawPassword = "1234";
		User user = new User(rawPassword);
		user = userRepository.save(user);

		List<Folder> folderList = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			folderList.add(
				FolderDto.CreateFolderReq.builder()
					.user(user)
					.title(i + "")
					.color(FolderColor.BLACK)
					.position(TEST_POSITION_VALUE1)
					.build()
					.toEntity()
			);
		}

		Folder savedFolder = Folder.builder().build();
		List<Folder> folderCount = folderRepository.findFirst20ByUserAndDeletedFalseOrDeletedNull(user);
		if (folderCount.size() > 20) {
			savedFolder = folderRepository.save(createFolderReq2.toEntity());
		}

		String title = savedFolder.getTitle();
		assertAll(
			() -> assertThat(title).isNotEqualTo(TEST_TITLE_VALUE2)
		);
	}

	@DisplayName("Folder 제목을 수정할 수 있다.")
	@Test
	public void updateTitle() {
		Folder beforeFolder1 = folderRepository.save(createFolderReq1.toEntity());
		Folder beforeFolder2 = folderRepository.save(createFolderReq2.toEntity());

		final FolderDto.UpdateFolderTitleReq req1 = FolderDto.UpdateFolderTitleReq.builder()
			.id(beforeFolder1.getId())
			.title(beforeFolder2.getTitle())
			.build();

		final FolderDto.UpdateFolderTitleReq req2 = FolderDto.UpdateFolderTitleReq.builder()
			.id(beforeFolder2.getId())
			.title(beforeFolder1.getTitle())
			.build();

		String beforeTitle1 = beforeFolder1.getTitle();
		String beforeTitle2 = beforeFolder2.getTitle();

		Folder afterFolder1 = updateFolderTitle(beforeFolder1.getId(), req1);
		String afterTitle1 = afterFolder1.getTitle();

		Folder afterFolder2 = updateFolderTitle(beforeFolder2.getId(), req2);
		String afterTitle2 = afterFolder2.getTitle();

		assertAll(
			() -> assertThat(beforeTitle1).isNotEqualTo(afterTitle1),
			() -> assertThat(beforeTitle2).isNotEqualTo(afterTitle2)
		);
	}

	// TODO: MOVE TO FolderService
	public Folder updateFolderTitle(Long id, FolderDto.UpdateFolderTitleReq dto) {
		final Folder folder = folderRepository.findById(id).get();
		folder.updateTitle(dto);
		return folder;
	}

	@DisplayName("Folder 순서를 수정할 수 있다.")
	@Test
	public void updatePosition() {
		Folder beforeFolder1 = folderRepository.save(createFolderReq1.toEntity());
		Folder beforeFolder2 = folderRepository.save(createFolderReq2.toEntity());

		final FolderDto.UpdateFolderPositionReq req1 = FolderDto.UpdateFolderPositionReq.builder()
			.id(beforeFolder1.getId())
			.position(beforeFolder2.getPosition())
			.build();

		final FolderDto.UpdateFolderPositionReq req2 = FolderDto.UpdateFolderPositionReq.builder()
			.id(beforeFolder2.getId())
			.position(beforeFolder1.getPosition())
			.build();

		List<FolderDto.UpdateFolderPositionReq> updatePositionReqList = new ArrayList<>();
		updatePositionReqList.add(req1);
		updatePositionReqList.add(req2);

		Integer beforePosition1 = beforeFolder1.getPosition();
		Integer beforePosition2 = beforeFolder2.getPosition();

		Folder afterFolder1 = updateFolderPosition(beforeFolder1.getId(), req1);
		Folder afterFolder2 = updateFolderPosition(beforeFolder2.getId(), req2);

		Integer afterPosition1 = afterFolder1.getPosition();
		Integer afterPosition2 = afterFolder2.getPosition();

		assertAll(
			() -> assertThat(beforePosition1).isNotEqualTo(afterPosition1),
			() -> assertThat(beforePosition2).isNotEqualTo(afterPosition2)
		);
	}

	// TODO: MOVE TO FolderService
	public Folder updateFolderPosition(Long id, FolderDto.UpdateFolderPositionReq dto) {
		final Folder folder = folderRepository.findById(id).get();
		folder.updatePosition(dto);
		return folder;
	}

	@DisplayName("Folder를 수정할 수 있다")
	@Test
	public void updateFolder() {
		Folder beforeFolder1 = folderRepository.save(createFolderReq1.toEntity());
		Folder beforeFolder2 = folderRepository.save(createFolderReq2.toEntity());

		final FolderDto.UpdateFolderReq req1 = FolderDto.UpdateFolderReq.builder()
			.id(beforeFolder1.getId())
			.title(beforeFolder2.getTitle())
			.position(beforeFolder2.getPosition())
			.color(beforeFolder2.getColor())
			.build();

		final FolderDto.UpdateFolderReq req2 = FolderDto.UpdateFolderReq.builder()
			.id(beforeFolder2.getId())
			.title(beforeFolder1.getTitle())
			.position(beforeFolder1.getPosition())
			.color(beforeFolder1.getColor())
			.build();

		List<FolderDto.UpdateFolderReq> req = new ArrayList<>();
		req.add(req1);
		req.add(req2);

		// req.stream().forEach(this::updateFolder);
		List<Folder> afterList = req.stream()
			.map(this::updateFolder)
			.collect(Collectors.toList());

		Folder afterFolder1 = afterList.get(0);
		Folder afterFolder2 = afterList.get(1);

		assertAll(
			() -> assertThat(beforeFolder1.getTitle()).isNotEqualTo(afterFolder1.getTitle()),
			() -> assertThat(beforeFolder2.getTitle()).isNotEqualTo(afterFolder2.getTitle())
		);
	}

	// TODO: MOVE TO FolderService
	public Folder updateFolder(FolderDto.UpdateFolderReq dto) {
		final Folder folder = folderRepository.findById(dto.getId()).get();
		folder.update(dto);
		return folder;
	}
}


