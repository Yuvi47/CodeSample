package com.example.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.app.dao.UserDao;

public interface UserService extends UserDetailsService {
	UserDao createUser(UserDao entity);

	UserDao getUser(String email);

	UserDao getUserByUserId(String userId);

	UserDao updateUser(String userId, UserDao userDao);
	
	void deleteUser(String userId);
	
	List<UserDao> getUsers(int page, int limit);

	boolean verifyEmailToken(String token);

	boolean requestPasswordReset(String email);

	boolean passwordReset(String token, String password);  

}
