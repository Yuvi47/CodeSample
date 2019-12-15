package com.example.app.security;

import com.example.app.SpringApplicationContext;

public class SecurityConstants {

	public static final long EXPIERTION_TIME = 864000000;
	public static final long PASSWORD_RESET_EXPIERTION_TIME = 3600000;
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String EMAIL_VERIFICATION_URL = "/users/email-verification";
	public static final String PASSWORD_REST_URL = "/users/password-reset";
	public static final String PASSWORD_RESTED = "/users/password-reseted";

	public static String getTokenSecret() {

		Properties properties = (Properties) SpringApplicationContext.getBean("Properties");
		return properties.getTokenSecret();
	}

}
