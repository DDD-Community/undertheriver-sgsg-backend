package com.undertheriver.sgsg.auth.common;

import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.undertheriver.sgsg.common.exception.AccessTokenLoadException;
import com.undertheriver.sgsg.common.exception.ExpiredTokenException;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private final String secretKey;
	private final long validityInMilliseconds;

	public JwtProvider(AppProperties appProperties) {
		String secret = appProperties.getAuth().getTokenSecret();
		this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
		this.validityInMilliseconds = appProperties.getAuth().getTokenExpirationMsec();
	}

	public String createToken(Long userId, UserRole userRole) {
		Claims claims = Jwts.claims();
		claims.put("userId", String.valueOf(userId));
		claims.put("role", userRole.name());

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public Claims extractValidSubject(String token) {
		validateToken(token);

		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private void validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			validateExpiredTime(claims);
		} catch (JwtException | IllegalArgumentException e) {
			throw new AccessTokenLoadException();
		}
	}

	private void validateExpiredTime(Jws<Claims> claims) {
		if (claims.getBody().getExpiration().before(new Date())) {
			throw new ExpiredTokenException();
		}
	}
}