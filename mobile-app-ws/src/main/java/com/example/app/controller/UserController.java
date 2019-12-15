package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.Resources;

import com.example.app.dao.AddressDao;
import com.example.app.dao.UserDao;
import com.example.app.model.PasswordBody;
import com.example.app.model.PasswordBodyModel;
import com.example.app.model.UserDetails;
import com.example.app.response.AddressRest;
import com.example.app.response.OperationStatusModel;
import com.example.app.response.RequestOperationStatus;
import com.example.app.response.UserRest;
import com.example.app.service.AddressService;
import com.example.app.service.UserService;

import java.lang.reflect.Type;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	// =====================================================================================================================//

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })

	public UserRest getUser(@PathVariable String id) {

		UserRest userRest = new UserRest();

		UserDao userDao = userService.getUserByUserId(id);

		BeanUtils.copyProperties(userDao, userRest);
		return userRest;
	}

	// ======================================================================================================================//

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetails userDetails) throws Exception {

		ModelMapper modelMapper = new ModelMapper();

		UserRest userRest = new UserRest();

		if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty())
			// || userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty())
			throw new NullPointerException("One of the obeject is null.");

		UserDao userDao = modelMapper.map(userDetails, UserDao.class);

		UserDao createdUser = userService.createUser(userDao);
		userRest = modelMapper.map(createdUser, UserRest.class);

		return userRest;
	}

	// ========================================================================================================================//

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetails userDetails) {

		   UserRest userRest = new UserRest();

		if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty())
			throw new NullPointerException("One of the obeject is null.");

		UserDao userDao = new UserDao();
		BeanUtils.copyProperties(userDetails, userDao);

		UserDao updatedUser = userService.updateUser(id, userDao);
		BeanUtils.copyProperties(updatedUser, userRest);
		return userRest;
	}

	// ========================================================================================================================//

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {

		OperationStatusModel statusModel = new OperationStatusModel();

		statusModel.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return statusModel;
	}

	// ==========================================================================================================================//

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUser(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		List<UserRest> returnValue = new ArrayList<>();
		List<UserDao> user = userService.getUsers(page, limit);

		for (UserDao userDao : user) {
			UserRest userModal = new UserRest();
			BeanUtils.copyProperties(userDao, userModal);

			returnValue.add(userModal);

		}

		return returnValue;
	}

	// ==========================================================================================================================//

	@GetMapping(path = "/{id}/address", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
			"application/hal+json" })
	public Resources<AddressRest> getUserAddress(@PathVariable String id) {

		List<AddressRest> addressRest = new ArrayList<AddressRest>();

		List<AddressDao> addressList = addressService.getUserAddress(id);

		if (addressList != null && !addressList.isEmpty()) {
			Type type = new TypeToken<List<AddressRest>>() {
			}.getType();

			addressRest = new ModelMapper().map(addressList, type);

			for (AddressRest addressRestList : addressRest) {

				Link addressLink = linkTo(
						methodOn(UserController.class).getUserAddressID(id, addressRestList.getAddressId()))
								.withSelfRel();
				addressRestList.add(addressLink);

				Link addressUserLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("users");

				addressRestList.add(addressUserLink);
			}

		}

		return new Resources<>(addressRest);
	}

	// ==========================================================================================================================//

	@GetMapping(path = "/{id}/address/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE, "application/hal+json" })
	public Resource<AddressRest> getUserAddressID(@PathVariable String id, @PathVariable String addressId) {

		ModelMapper modelMapper = new ModelMapper();

		AddressRest addressRest = new AddressRest();

		AddressDao adressList = addressService.getUserAddressId(addressId);

		Link addressLink = linkTo(methodOn(UserController.class).getUserAddressID(id, addressId)).withSelfRel();
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddress(id)).withRel("addresses");
		Link usersLink = linkTo(UserController.class).slash(id).withRel("users");

		if (adressList != null) {

			addressRest = modelMapper.map(adressList, AddressRest.class);

			addressRest.add(addressLink);
			addressRest.add(usersLink);
			addressRest.add(addressesLink);
		}

		return new Resource<>(addressRest);
	}

	// ==========================================================================================================================//

	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

		OperationStatusModel statusModel = new OperationStatusModel();

		statusModel.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

		boolean isVerfied = userService.verifyEmailToken(token);

		if (isVerfied) {
			statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
		} else {
			statusModel.setOperationResult(RequestOperationStatus.ERROR.name());
		}

		return statusModel;
	}

	// ==========================================================================================================================//

	@PostMapping(path = "/password-reset", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel passwordVerify(@RequestBody PasswordBody passwordBody) {

		OperationStatusModel modelResult = new OperationStatusModel();

		boolean b = userService.requestPasswordReset(passwordBody.getEmail());

		modelResult.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		modelResult.setOperationResult(RequestOperationStatus.ERROR.name());

		if (b) {

			modelResult.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		return modelResult;
	}

	@PostMapping(path = "/password-reseted", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel passwordVerified(@RequestBody PasswordBodyModel passwordBodyModel) {

		OperationStatusModel retuenValue = new OperationStatusModel();

		boolean op = userService.passwordReset(passwordBodyModel.getToken(), passwordBodyModel.getPassword());

		retuenValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
		retuenValue.setOperationResult(RequestOperationStatus.ERROR.name());

		if (op) {	

			retuenValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		return retuenValue;
	}

}
