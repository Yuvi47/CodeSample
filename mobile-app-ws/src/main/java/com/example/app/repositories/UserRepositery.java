package com.example.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.example.app.entity.UserEntity;

@Repository
public interface UserRepositery extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {
	
	
	UserEntity findUserByEmail (String email);
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);

}
