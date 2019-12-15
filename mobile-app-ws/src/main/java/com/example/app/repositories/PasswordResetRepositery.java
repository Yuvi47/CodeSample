package com.example.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.entity.PasswordResetEntitiy;

public interface PasswordResetRepositery extends JpaRepository<PasswordResetEntitiy, Long> {

	PasswordResetEntitiy findByToken(String token);

	
}
