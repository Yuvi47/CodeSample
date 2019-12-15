package com.example.app.serviceimple;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.AddressDao;
import com.example.app.entity.AddressEntity;
import com.example.app.entity.UserEntity;
import com.example.app.repositories.AddressRepositery;
import com.example.app.repositories.UserRepositery;
import com.example.app.service.AddressService;

@Service
public class AddressServiceImple implements AddressService {

	@Autowired
	UserRepositery userRepositery;

	@Autowired
	AddressRepositery addressRepositery;

	public List<AddressDao> getUserAddress(String userId) {

		ModelMapper mapper = new ModelMapper();

		List<AddressDao> list = new ArrayList<AddressDao>();

		UserEntity userEntity = userRepositery.findByUserId(userId);

		if (userEntity == null)
			return list;

		Iterable<AddressEntity> iterable = addressRepositery.findUserByuserEntity(userEntity);

		for (AddressEntity addressEntity : iterable) {

			list.add(mapper.map(addressEntity, AddressDao.class));
		}

		return list;
	}

	@Override
	public AddressDao getUserAddressId(String addressId) {

		ModelMapper mapper = new ModelMapper();
		
		AddressDao addressDao = new AddressDao();

		AddressEntity addressEntity = addressRepositery.findByaddressId(addressId);

		if (addressEntity == null) {
			return addressDao;
		}
		
		addressDao = mapper.map(addressEntity, AddressDao.class);
		

		return addressDao;
	}

}
