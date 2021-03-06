package com.koda.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@Before
	public void setup() {
		RestAssured.port = 8080;
	}
	
	@Test
	public void default_path_접근시_index호출() throws Exception {
		given()
			.when()
				.get("/")
			.then()
				.statusCode(200)
				.contentType("text/html")
				.body(containsString("권한 관리"));
	}

}

