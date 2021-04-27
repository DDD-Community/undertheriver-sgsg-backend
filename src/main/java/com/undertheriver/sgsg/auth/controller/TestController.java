package com.undertheriver.sgsg.auth.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/callback")
	public Map<String, Object> callback(@RequestParam Map<String, Object> param) {
		return param;
	}
}
