package com.example.app.dao;

import java.io.Serializable;
import java.util.List;

public class UserDao implements Serializable {

	private static final long serialVersionUID = -3648742592872991521L;

	private long Id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String passWord;
	private String encryptedPassword;
	private String emailVerificationToken;
	private boolean emailVerificationStatus = false;
	private List<AddressDao> addresses;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return passWord;
	}

	public void setPassword(String password) {
		passWord = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public boolean isEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public List<AddressDao> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDao> addresses) {
		this.addresses = addresses;
	}

}
