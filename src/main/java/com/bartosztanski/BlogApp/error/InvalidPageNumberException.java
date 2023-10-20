package com.bartosztanski.BlogApp.error;

public class InvalidPageNumberException extends Exception {

	private static final long serialVersionUID = 1780412439207061205L;

	public InvalidPageNumberException() {
		super();
	}
	
	public InvalidPageNumberException(String message) {
		super(message);
	}
	
	public InvalidPageNumberException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidPageNumberException(Throwable cause) {
		super(cause);
	}
	
	public InvalidPageNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
