package com.example.app.controller;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.app.dao.AddressDao;
import com.example.app.dao.UserDao;
import com.example.app.response.UserRest;
import com.example.app.serviceimple.UserServiceImple;

import java.util.*;

class UserControllerTest {

	@InjectMocks
	UserController uc;

	@Mock
	UserServiceImple imple;

	UserDao userDao;

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		userDao = new UserDao();
		userDao.setFirstName("Se");
		userDao.setLastName("Kar");
		userDao.setEmail("test@test.com");
		userDao.setEmailVerificationStatus(Boolean.FALSE);
		userDao.setEmailVerificationToken(null);
		userDao.setUserId("12345");
		userDao.setAddresses(getAddressesDao());
		userDao.setEncryptedPassword("xcf58tugh47");
	}

	@Test
	void testGetUserString() {
		when(imple.getUserByUserId(anyString())).thenReturn(userDao);

		UserRest rest = uc.getUser(userDao.getUserId());
		assertNotNull(rest);
		assertEquals(userDao.getUserId(), rest.getUserId());
		assertEquals(userDao.getFirstName(), rest.getFirstName());
		assertEquals(userDao.getLastName(), rest.getLastName());
		assertEquals(userDao.getLastName(), rest.getLastName());
		assertEquals(userDao.getAddresses().size(), rest.getAddresses().size());

	}

	private List<AddressDao> getAddressesDao() {
		AddressDao addressDao = new AddressDao();
		addressDao.setType("shipping");
		addressDao.setCity("Vancouver");
		addressDao.setCountry("Canada");
		addressDao.setZipCode(123);
		addressDao.setStreetName("123 Street name");

		AddressDao billingAddressDao = new AddressDao();
		billingAddressDao.setType("billling");
		billingAddressDao.setCity("Vancouver");
		billingAddressDao.setCountry("Canada");
		billingAddressDao.setZipCode(123);
		billingAddressDao.setStreetName("123 Street name");

		List<AddressDao> addresses = new ArrayList<>();
		addresses.add(addressDao);
		addresses.add(billingAddressDao);

		return addresses;

	}

}
