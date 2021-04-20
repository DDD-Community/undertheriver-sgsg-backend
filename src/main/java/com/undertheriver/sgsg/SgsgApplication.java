package com.undertheriver.sgsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.undertheriver.sgsg.config.PagingConfig;

@EnableConfigurationProperties(PagingConfig.class)
@SpringBootApplication
public class SgsgApplication {
	public static void main(String[] args) {
		SpringApplication.run(SgsgApplication.class, args);
	}
}
