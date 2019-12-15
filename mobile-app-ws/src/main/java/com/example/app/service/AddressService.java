package com.example.app.service;

import java.util.List;

import com.example.app.dao.AddressDao;


public interface AddressService  {

	List<AddressDao> getUserAddress(String userId);

	AddressDao getUserAddressId(String addressId);
}
