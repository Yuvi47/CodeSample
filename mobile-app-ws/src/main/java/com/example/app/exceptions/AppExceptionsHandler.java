package com.example.app.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.app.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {
	 @ExceptionHandler(value = UserServiceException.class)
	 public ResponseEntity<Object>
	 handlerUserServiceException(UserServiceException exception, WebRequest
	 webRequest) {
	
	 ErrorMessage errorMessage = new ErrorMessage(new Date(),
	 exception.getMessage());
	
	 return new ResponseEntity<>(errorMessage.getMessage(), new HttpHeaders(),
	 HttpStatus.INTERNAL_SERVER_ERROR);
	 }

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> handlerOtherUserServiceException(UserServiceException exception,
			WebRequest webRequest) {

		ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());

		return new ResponseEntity<>(errorMessage.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}