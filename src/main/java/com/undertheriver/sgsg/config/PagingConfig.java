package com.undertheriver.sgsg.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@ConfigurationProperties(prefix = "paging")
public class PagingConfig {

	private Map<String, Integer> folder;
	private Map<String, Integer> memo;
	private Integer maxRow;
	private Integer minPage;

	public PagingConfig(Map<String, Integer> folder, Map<String, Integer> memo, Integer maxRow, Integer minPage) {
		this.folder = folder;
		this.memo = memo;
		this.maxRow = maxRow;
		this.minPage = minPage;
	}

	public PagingConfig(Map<String, Integer> folder, Map<String, Integer> memo) {
		this.folder = folder;
		this.memo = memo;
	}
}
