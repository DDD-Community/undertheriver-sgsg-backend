package com.undertheriver.sgsg.memo.domain.dto;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.undertheriver.sgsg.foler.domain.FolderColor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemoDto {
	@Setter
	@Getter
	@NoArgsConstructor
	public static class CreateMemoReq {
		@Nullable
		Long folderId;
		@NotNull
		String folderTitle;
		@NotNull
		FolderColor folderColor;
		@NotNull
		String memoContent;

		@Builder
		public CreateMemoReq(Long folderId, String folderTitle, FolderColor folderColor, String memoContent) {
			this.folderId = folderId;
			this.folderTitle = folderTitle;
			this.folderColor = folderColor;
			this.memoContent = memoContent;
		}
	}
}
