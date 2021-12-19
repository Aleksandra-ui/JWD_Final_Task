package com.epam.jwd.apotheca.exception;

public class CouldNotInitializeConnectionPoolException extends Exception {

	private static final long serialVersionUID = -6270552880462489272L;

	public CouldNotInitializeConnectionPoolException(String message, Throwable cause) {
		super(message, cause);
	}
	
}