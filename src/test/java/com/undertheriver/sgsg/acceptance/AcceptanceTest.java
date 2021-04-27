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

	private String token;

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.email("test@test.com")
			.profileImageUrl("http://naver.com/adf.png")
			.userRole(UserRole.USER)
			.name("TEST")
			.userSecretMemoPassword("1234")
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
				.statusCode(HttpStatus.SC_OK)
				.log()
					.all()
				.extract()
					.as(classType);
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
				.statusCode(HttpStatus.SC_OK)
				.log()
					.all()
				.extract()
					.jsonPath()
						.getList(".", classType);
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
			.statusCode(HttpStatus.SC_OK)
			.log()
			.all();
		//@formatter:on
	}
}
