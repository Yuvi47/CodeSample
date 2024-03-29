package com.example.app.model;

import java.util.List;

public class UserDetails {

	private String firstName;
	private String lastName;
	private String email;
	private String Password;
	private List<RequestAddress> addresses;
	

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
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public List<RequestAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<RequestAddress> addresses) {
		this.addresses = addresses;
	}

}
