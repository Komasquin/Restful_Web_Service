package com.rest.store002.we.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.rest.store002.ui.model.response.ErrorMessage;

//This class will be used to format the exceptions
@ControllerAdvice
public class AppExceptionsHandler {

	//This class will handle exceptions for the provided values in the annotation values 
	@ExceptionHandler(value = {UserServiceException.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request ) {
		
		//First: create an object from our ErrorMessage class
		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		
		//Last: return an object of ResponseEntity that takes our exception as a parameter, along with the header, and status message
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//This method handles exceptions that we have not made a custom exception for
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest req){
		//First: create an object from our ErrorMessage class
		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		
		//Last: return an object of ResponseEntity that takes our exception as a parameter, along with the header, and status message
		//return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
