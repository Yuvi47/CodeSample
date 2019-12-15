package com.example.app.serviceimple;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.app.dao.AddressDao;
import com.example.app.dao.UserDao;
import com.example.app.entity.UserEntity;
import com.example.app.exceptions.UserServiceException;
import com.example.app.repositories.UserRepositery;
import com.example.app.utils.AmazonSimpleService;
import com.example.app.utils.Util;

class UserServiceImpleTest {

	@InjectMocks
	UserServiceImple imple;

	@Mock
	Util util;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	UserRepositery repo;

	@Mock
	AmazonSimpleService ses;

	UserEntity en;

	@BeforeEach
	void setUp() throws Exception {

		en = new UserEntity();
		en.setId(1L);
		en.setFirstName("hello");
		en.setUserId("1234");
		en.setEncryptedPassword("124333");
		en.setEmailVerificationToken("qwqw");
		en.setEmail("test@test.com");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUser() {

		when(repo.findByEmail(anyString())).thenReturn(en);

		UserDao dao = imple.getUser("test@test.com");

		assertNotNull(dao);
		assertEquals("hello", dao.getFirstName());
	}

	@Test
	void testGetUser_UsernameNotFoundException() {

		when(repo.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			imple.getUser("test@test.com");
		});
	}

	@Test
	void testCreateUser_RuntimeException() {

		UserDao dao = new UserDao();
		dao.setEmail("test");
		when(repo.findByEmail(anyString())).thenReturn(en);

		assertThrows(UserServiceException.class, () -> {
			imple.createUser(dao);
		});
	}

	@Test
	void testCreateUser() {

		when(repo.findByEmail(anyString())).thenReturn(null);
		when(util.generaterAddressId(anyInt())).thenReturn("12345");
		when(util.generaterUserId(anyInt())).thenReturn("00000");
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("abcd");
		when(repo.save(any())).thenReturn(en);

		AddressDao adao = new AddressDao();

		adao.setType("Shipping");

		List<AddressDao> adds = new ArrayList<AddressDao>();
		adds.add(adao);

		UserDao dao = new UserDao();
		dao.setAddresses(adds);

		UserDao dao2 = imple.createUser(dao);

		assertNotNull(dao2);
		assertEquals(en.getFirstName(), dao2.getFirstName());
	}

}
