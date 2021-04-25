package com.undertheriver.sgsg.memo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.undertheriver.sgsg.memo.domain.dto.MemoDto;

@SpringBootTest
class MemoRepositoryTest {
	@Autowired
	private MemoRepository memoRepository;

	@DisplayName("폴더가 있을 때 메모를 생성할 수 있다.")
	@Test
	public void create() {
		MemoDto.CreateMemoReq
	}

	@DisplayName("폴더가 없을 때 메모를 생성할 수 있다.")

}