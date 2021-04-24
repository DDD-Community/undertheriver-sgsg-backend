package com.undertheriver.sgsg.config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PagingConfigTest {
	@Autowired
	private PagingConfig pagingConfig;

	@DisplayName("application.yml 의 paging 값을 가져올 수 있다.")
	@Test
	public void readProperty() {
		Map<String, Integer> folder = pagingConfig.getFolderConfig();
		Map<String, Integer> memo = pagingConfig.getMemoConfig();

		assertAll(
			() -> assertThat(folder.get("limit")).isEqualTo(20),
			() -> assertThat(memo.get("limit")).isEqualTo(20)
		);
	}
}