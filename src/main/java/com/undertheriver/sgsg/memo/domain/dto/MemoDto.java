package com.undertheriver.sgsg.memo.domain.dto;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.undertheriver.sgsg.foler.domain.FolderColor;

import lombok.Builder;
import lombok.Getter;

public class MemoDto {
	@Getter
	public static class CreateMemoReq {
		@Nullable
		Long folderId;
		@Nullable
		FolderColor folderColor;
		@NotNull
		String content;

		@Builder
		public CreateMemoReq(@Nullable Long folderId, @Nullable FolderColor folderColor,
			@NotNull String content) {
			this.folderId = folderId;
			this.folderColor = folderColor;
			this.content = content;
		}
	}
}
