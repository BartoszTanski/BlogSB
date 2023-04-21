package com.bartosztanski.BlogApp.error;

public class VideoNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -625787476383619763L;
	
	public VideoNotFoundException() {
		super();
	}
	
	public VideoNotFoundException(String message) {
		super(message);
	}
	
	public VideoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public VideoNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public VideoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
