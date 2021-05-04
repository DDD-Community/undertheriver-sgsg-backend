package com.undertheriver.sgsg.memo.domain.dto;

import javax.validation.constraints.NotNull;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.memo.domain.Memo;
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

		public Memo toMemoEntity() {
			return Memo.builder()
					.content(memoContent)
					.build();
		}

		public Folder toFolderEntity() {
			return Folder.builder()
				.title(folderTitle)
				.color(folderColor)
				.build();
		}

		public boolean hasFolderId() {
			return folderId != null;
		}
	}
}
