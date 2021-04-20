package com.undertheriver.sgsg.common.dto;

import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public final class PageRequest {
	private static final Integer MAX_SIZE = 50;

	private int page;
	private int size;
	private Sort.Direction direction;

	public void setPage(int page) {
		this.page = page <= 0 ? 1 : page;
	}

	public void setSize(int size) {
		this.size = size > MAX_SIZE ? MAX_SIZE : size;
	}

	public void setDirection(Sort.Direction direction) {
		this.direction = direction;
	}

	// public PageRequest of(String columnName) {
	// 	return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, columnName);
	// }
}
