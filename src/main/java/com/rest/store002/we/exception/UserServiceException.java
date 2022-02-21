package com.rest.store002.we.exception;

//This class is used to create custom error messages inside 'AppExceptionsHandler' class
public class UserServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9200587642329934466L;

	public UserServiceException(String message) {
		super(message);
	}
}
