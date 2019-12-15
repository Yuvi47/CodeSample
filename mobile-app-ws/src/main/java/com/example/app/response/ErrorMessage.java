package com.example.app.response;

import java.util.Date;

public class ErrorMessage {

	private Date timeStamp;
	private String message;

	public ErrorMessage(Date date, String message2) {
		// TODO Auto-generated constructor stub
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
