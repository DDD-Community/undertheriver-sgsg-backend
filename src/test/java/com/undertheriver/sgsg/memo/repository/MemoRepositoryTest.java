package com.undertheriver.sgsg.memo.repository;

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
import com.undertheriver.sgsg.memo.domain.dto.MemoDto;
import com.undertheriver.sgsg.user.domain.User;
import com.undertheriver.sgsg.user.domain.UserRepository;

@SpringBootTest
class MemoRepositoryTest {
	@Autowired
	private MemoRepository memoRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private FolderService folderService;

	User user;
	Folder folder;
	FolderDto.CreateFolderReq createFolderReq1;
	MemoDto.CreateMemoReq createMemoReq1;

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
		Long folderId = folderService.save(user.getId(), createFolderReq1);
		folder = folderRepository.findById(folderId).get();
		createMemoReq1 = MemoDto.CreateMemoReq.builder()
			.folderId(folder.getId())
			.folderTitle(folder.getTitle())
			.content("메모입니다 메모")
			.build();
	}
}