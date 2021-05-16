package com.undertheriver.sgsg.acceptance;

import static io.restassured.RestAssured.*;

import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.undertheriver.sgsg.auth.common.JwtProvider;
import com.undertheriver.sgsg.common.type.UserRole;
import com.undertheriver.sgsg.user.domain.User;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/query/user.sql")
@Sql(scripts = "/query/user-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class AcceptanceTest {

	@LocalServerPort
	protected int port;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	protected ObjectMapper objectMapper;

	private String token;

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.email("test@test.com")
			.profileImageUrl("http://naver.com/adf.png")
			.userRole(UserRole.USER)
			.name("TEST")
			.build();

		token = jwtProvider.createToken(1L, user.getUserRole());

		if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
		}
	}

	protected <T> T getOne(String url, Class<T> classType) {
		//@formatter:off
		return
			given()
			.when()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.auth()
					.oauth2(token)
				.get(url)
			.then()
				.log()
					.all()
				.statusCode(HttpStatus.SC_OK)
				.extract()
					.jsonPath()
						.getObject("response", classType);
		//@formatter:on
	}

	protected <T> List<T> getAll(String url, Class<T> classType) {
		//@formatter:off
		return
			given()
			.when()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.auth()
					.oauth2(token)
				.get(url)
			.then()
				.log()
					.all()
				.statusCode(HttpStatus.SC_OK)
				.extract()
					.jsonPath()
						.getList("response", classType);
		//@formatter:on
	}

	protected void post(String url, String json) {
		//@formatter:off
		given()
			.body(json)
			.when()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.auth()
			.oauth2(token)
			.post(url)
			.then()
			.log()
			.all()
			.statusCode(HttpStatus.SC_OK);
		//@formatter:on
	}

	protected void delete(String url) {
		//@formatter:off
		given()
		.when()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.auth()
				.oauth2(token)
			.delete(url)
		.then()
			.log()
				.all()
			.statusCode(HttpStatus.SC_OK);
		//@formatter:on
	}

	protected void put(String url, String json) {
		//@formatter:off
		given()
			.body(json)
		.when()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.auth()
				.oauth2(token)
			.put(url)
		.then()
			.log()
				.all()
			.statusCode(HttpStatus.SC_OK);
		//@formatter:on
	}
}

