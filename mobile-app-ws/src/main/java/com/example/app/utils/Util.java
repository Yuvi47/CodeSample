package com.example.app.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.app.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Util {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String generaterUserId(int lenght) {

		return generaterRandomString(lenght);
	}

	public String generaterAddressId(int lenght) {

		return generaterRandomString(lenght);
	}

	private String generaterRandomString(int lenght) {
		StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; i++) {

			stringBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(stringBuilder);
	}

	public static boolean hasTokenExpired(String token) {
		boolean returnValue = false;
		try {
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody();
			Date tokenExpiretionDate = claims.getExpiration();
			Date todayDate = new Date();

			returnValue = tokenExpiretionDate.before(todayDate);

		} catch (ExpiredJwtException ex) {
			return true;
		}
		return returnValue;

	}

	public String generateEmailVerficationToken(String string) {
		String token = Jwts.builder().setSubject(string)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIERTION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

		return token;
	}

	public String generatePasswordRestToken(String string) {

		String token = Jwts.builder().setSubject(string)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIERTION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

		return token;
	}
}
