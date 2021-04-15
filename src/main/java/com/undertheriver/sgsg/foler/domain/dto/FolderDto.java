package com.undertheriver.sgsg.foler.domain.dto;

import com.undertheriver.sgsg.foler.domain.Folder;
import com.undertheriver.sgsg.foler.domain.FolderColor;
import com.undertheriver.sgsg.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FolderDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateFolderReq {
		private String title;
		private FolderColor color;
		private Integer position;
		private User user;

		@Builder
		public CreateFolderReq(String title, FolderColor color, Integer position, User user) {
			this.title = title;
			this.color = color;
			this.position = position;
			this.user = user;
		}

		public Folder toEntity() {
			return Folder.builder()
				.title(this.title)
				.color(this.color)
				.position(this.position)
				.user(this.user)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateFolderTitleReq {
		private Long id;
		private String title;

		@Builder
		public UpdateFolderTitleReq(Long id, String title) {
			this.id = id;
			this.title = title;
		}

		public Folder toEntity() {
			return Folder.builder()
				.title(this.title)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateFolderPositionReq {
		private Long id;
		private Integer position;

		@Builder
		public UpdateFolderPositionReq(Long id, Integer position) {
			this.id = id;
			this.position = position;
		}

		public Folder toEntity() {
			return Folder.builder()
				.position(this.position)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateFolderReq {
		private Long id;
		private String title;
		private Integer position;
		private FolderColor color;

		@Builder
		public UpdateFolderReq(Long id, String title, Integer position, FolderColor color) {
			this.id = id;
			this.title = title;
			this.position = position;
			this.color = color;
		}

		public Folder toEntity() {
			return Folder.builder()
				.title(this.title)
				.color(this.color)
				.position(this.position)
				.build();
		}
	}
}
