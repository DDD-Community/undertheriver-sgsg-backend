package com.undertheriver.sgsg;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.undertheriver.sgsg.config.security.UserPrincipal;

@RestController
public class HealthController {

	@GetMapping("/health")
	public ResponseEntity<Void> health() {
		return ResponseEntity.noContent()
			.build();
	}

	@GetMapping("/")
	public ResponseEntity<String> hello(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		System.out.println(userPrincipal);
		return ResponseEntity
			.ok("Hello World!");
	}
}
