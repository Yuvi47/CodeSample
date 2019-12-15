package com.example.app.response;

public enum ErrorMessages {

	
	MISSING_REQUIRED_FIELD("Missing required field. Please check it again."),
	RECORD_ALREADY_EXISTS("Record already exists."),
	INTERNAL_SERVER_ERROR("Internal Server error."),
	NO_RECORD_FOUND("There is no record found."),
	AUTHENTICATION_FAILD("Authentication is faild"),
	COULD_NOT_UPDATE_RECORD("Your record not update"),
	COULD_NOT_DELETE_RECORD("Your Record not deleted"),
	EMAIL_ADDRESS_NOT_VERIFIED("Your email is not verified");
	
	private String errorMessage;

	ErrorMessages(String error) {
		this.errorMessage = error;
	}
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
