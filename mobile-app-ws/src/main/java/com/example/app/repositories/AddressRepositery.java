package com.example.app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.AddressEntity;
import com.example.app.entity.UserEntity;

@Repository
public interface AddressRepositery extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findUserByuserEntity(UserEntity userEntity);

	AddressEntity findByaddressId(String addressId);
}
