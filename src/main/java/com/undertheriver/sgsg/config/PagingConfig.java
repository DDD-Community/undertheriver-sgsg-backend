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

    private Map<String, Integer> folderConfig;
    private Map<String, Integer> memoConfig;

    public PagingConfig(Map<String, Integer> folderConfig, Map<String, Integer> memoConfig) {
        this.folderConfig = folderConfig;
        this.memoConfig = memoConfig;
    }
}
