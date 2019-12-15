package com.example.app.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilTest {

	@Autowired
	Util util;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGeneraterUserId() {

		String user1 = util.generaterUserId(30);
		String user2 = util.generaterUserId(30);

		assertNotNull(user1);
		assertNotNull(user2);
		assertEquals(user1.length(), user2.length());
	}

	@Test
	@Disabled
	void testHasTokenExpired() {
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzMjc3Nzc3NX0.cdudUo3pwZLN9UiTuXiT7itpaQs6BgUPU0yWbNcz56-l1Z0476N3H_qSEHXQI5lUfaK2ePtTWJfROmf0213UJA";
		boolean hasTokenExpired = Util.hasTokenExpired(expiredToken);

		assertTrue(hasTokenExpired);
	}

	@Test
	final void testHasTokenNotExpired() {
		String token = util.generateEmailVerficationToken("4yr65hhyid84");
		assertNotNull(token);

		boolean hasTokenExpired = Util.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}

}
