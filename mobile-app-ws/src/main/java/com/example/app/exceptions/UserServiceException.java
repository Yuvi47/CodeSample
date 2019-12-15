package com.example.app.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -8800418520999992076L;

	public UserServiceException(String message) {
		super(message);
	}
}
