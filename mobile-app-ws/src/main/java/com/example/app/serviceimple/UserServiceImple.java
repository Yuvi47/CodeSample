package com.example.app.serviceimple;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dao.AddressDao;
import com.example.app.dao.UserDao;
import com.example.app.entity.PasswordResetEntitiy;
import com.example.app.entity.UserEntity;
import com.example.app.exceptions.UserServiceException;
import com.example.app.repositories.PasswordResetRepositery;
import com.example.app.repositories.UserRepositery;
import com.example.app.response.ErrorMessages;
import com.example.app.service.UserService;
import com.example.app.utils.AmazonSimpleService;
import com.example.app.utils.Util;

@Transactional
@Service
public class UserServiceImple implements UserService  {

	@Autowired
	UserRepositery userRepositery;

	@Autowired
	Util util;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	PasswordResetRepositery passwordResetRepositery;

	@Override
	public UserDao createUser(UserDao dao) {

		UserEntity storedCheck = userRepositery.findByEmail(dao.getEmail());

		ModelMapper modelMapper = new ModelMapper();
		if (storedCheck == null) {

			for (int i = 0; i < dao.getAddresses().size(); i++) {

				AddressDao adressList = dao.getAddresses().get(i);
				adressList.setUserDao(dao);
				adressList.setAddressId(util.generaterAddressId(6));
				dao.getAddresses().set(i, adressList);

			}

			String string = util.generaterUserId(4);

			UserEntity entity = new UserEntity();

			UserDao returnValue = new UserDao();

			entity = modelMapper.map(dao, UserEntity.class);

			// BeanUtils.copyProperties(dao, entity);

			entity.setEncryptedPassword(bCryptPasswordEncoder.encode(dao.getPassword()));

			entity.setUserId(string);

			entity.setEmailVerificationToken(util.generateEmailVerficationToken(string));
			entity.setEmailVerificationStatus(false);

			UserEntity userEntity = userRepositery.save(entity);

			// BeanUtils.copyProperties(userEntity, returnValue);

			returnValue = modelMapper.map(userEntity, UserDao.class);

			new AmazonSimpleService().verify(returnValue);

			return returnValue;

		} else {
			throw new UserServiceException("Data already exists");
		}
	}

	// ==========================================================================================================//

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepositery.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		{

			// return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new
			// ArrayList<>());

			return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
					userEntity.isEmailVerificationStatus(), true, true, true, new ArrayList<>());
		}

	}

	// =================================================================================================================//

	@Override
	public UserDao getUser(String email) {
		UserEntity userEntity = userRepositery.findByEmail(email);

		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDao returnValue = new UserDao();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	// ============================================================================================================//

	@Override
	public UserDao getUserByUserId(String userId) {
		UserEntity userEntity = userRepositery.findByUserId(userId);
		if (userEntity == null) {
			throw new UsernameNotFoundException(userId);
		}

		UserDao returnValue = new UserDao();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;

	}

	// ===================================================================================================//

	@Override
	public UserDao updateUser(String userId, UserDao userDao) {
		UserEntity userEntity = userRepositery.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		UserDao returnValue = new UserDao();

		userEntity.setFirstName(userDao.getFirstName());
		userEntity.setLastName(userDao.getLastName());
		UserEntity updateEntity = userRepositery.save(userEntity);
		BeanUtils.copyProperties(updateEntity, returnValue);
		return returnValue;
	}

	// ============================================================================================================//

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepositery.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		userRepositery.delete(userEntity);
	}

	// =====================================================================================================//

	@Override
	public List<UserDao> getUsers(int page, int limit) {

		List<UserDao> returnValue = new ArrayList<>();

		if (page > 0) {
			page = page - 1;
		}

		Pageable pageRequest = PageRequest.of(page, limit);

		Page<UserEntity> userPage = userRepositery.findAll(pageRequest);

		List<UserEntity> user = userPage.getContent();

		for (UserEntity userEntity : user) {
			UserDao dao = new UserDao();

			BeanUtils.copyProperties(userEntity, dao);
			returnValue.add(dao);
		}

		return returnValue;
	}

	// ========================================================================//
	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		UserEntity userEntity = userRepositery.findUserByEmailVerificationToken(token);

		if (userEntity != null) {
			boolean hastokenExpired = Util.hasTokenExpired(token);
			if (!hastokenExpired) {

				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepositery.save(userEntity);
				returnValue = true;

			}
		}
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		boolean returnValue = false;

		UserEntity entity = userRepositery.findByEmail(email);
		if (entity == null) {
			return returnValue;
		}

		String token = new Util().generatePasswordRestToken(entity.getUserId());

		PasswordResetEntitiy passwordResetEntitiy = new PasswordResetEntitiy();

		passwordResetEntitiy.setToken(token);
		passwordResetEntitiy.setEntity(entity);
		passwordResetRepositery.save(passwordResetEntitiy);
		returnValue = new AmazonSimpleService().sendPasswordResetRequest(entity.getFirstName(), entity.getEmail(),
				token);
		return returnValue;
	}

	@Override
	public boolean passwordReset(String token, String password) {

		boolean returnValue = false;

		if (Util.hasTokenExpired(token)) {

			return returnValue;
		}

		PasswordResetEntitiy entitiyPassword = passwordResetRepositery.findByToken(token);

		if (entitiyPassword == null) {
			return returnValue;
		}

		String enCodedPassword = bCryptPasswordEncoder.encode(password);

		UserEntity userEntity = entitiyPassword.getEntity();
		userEntity.setEncryptedPassword(enCodedPassword);
		UserEntity savedUserEntity = userRepositery.save(userEntity);

		if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(enCodedPassword)) {

			return returnValue = true;
		}

		passwordResetRepositery.delete(entitiyPassword);

		return returnValue;
	}

}
