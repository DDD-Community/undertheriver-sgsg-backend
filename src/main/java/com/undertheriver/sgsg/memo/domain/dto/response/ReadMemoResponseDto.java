package com.undertheriver.sgsg.memo.domain.dto.response;

import lombok.Getter;

@Getter
public class ReadMemoResponseDto {
	Long id;
	String content;
	Integer contentLength;
	String createdAt;
	Boolean isSecret;
	Boolean isFavorite;
	String ThumbnailUrl;
}
