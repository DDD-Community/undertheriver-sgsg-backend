package com.undertheriver.sgsg.memo.domain.dto;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.undertheriver.sgsg.foler.domain.FolderColor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemoDto {
	@Getter
	@NoArgsConstructor
	public static class CreateMemoReq {
		@Nullable
		Long folderId;
		@NotNull
		String content;

		@Builder
		public CreateMemoReq(@Nullable Long folderId, @NotNull String content) {
			this.folderId = folderId;
			this.content = content;
		}
	}
}
