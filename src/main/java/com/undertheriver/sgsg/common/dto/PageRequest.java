package com.undertheriver.sgsg.common.dto;

import java.util.Map;

import org.springframework.data.domain.Sort;

import com.undertheriver.sgsg.config.PagingConfig;

import lombok.Getter;

@Getter
public final class PageRequest {

	private int page;
	private int size;
	private Sort.Direction direction;
	private String orderBy;

	public PageRequest(int page, int size, Sort.Direction direction, String orderBy) {
		this.page = page;
		this.size = size;
		this.direction = direction;
		this.orderBy = orderBy;
	}

	public PageRequest(Map<String, Integer> config) {
		this.page = 1;
		this.size = config.get("limit");
	}

	public void setPage(int page) {
		this.page = page <= 0 ? 1 : page;
	}

	public void setSize(int size) {
		int DEFAULT_SIZE = 10;
		int MAX_SIZE = 50;
		this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
	}

	public void setDirection(Sort.Direction direction) {
		this.direction = direction;
	}

	public void setOrderBy(String orderBy) { this.orderBy = orderBy; }

	public org.springframework.data.domain.PageRequest of(Sort.Direction direction, String orderBy) {
		return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, orderBy);
	}

	public org.springframework.data.domain.PageRequest of() {
		return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, orderBy);
	}
}
