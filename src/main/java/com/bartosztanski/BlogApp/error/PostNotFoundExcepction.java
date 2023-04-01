package com.bartosztanski.BlogApp.error;

public class PostNotFoundExcepction extends java.lang.Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4457436238865514546L;

	public PostNotFoundExcepction() {
		super();
	}
	
	public PostNotFoundExcepction(String message) {
		super(message);
	}
	
	public PostNotFoundExcepction(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PostNotFoundExcepction(Throwable cause) {
		super(cause);
	}
	
	public PostNotFoundExcepction(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
