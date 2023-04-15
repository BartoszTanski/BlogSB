package com.bartosztanski.BlogApp.error;

public class PostInsertFailedException extends java.lang.Exception {

	private static final long serialVersionUID = 3025123994197501110L;
	
	public PostInsertFailedException() {
		super();
	}
	
	public PostInsertFailedException(String message) {
		super(message);
	}
	
	public PostInsertFailedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PostInsertFailedException(Throwable cause) {
		super(cause);
	}
	
	public PostInsertFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

