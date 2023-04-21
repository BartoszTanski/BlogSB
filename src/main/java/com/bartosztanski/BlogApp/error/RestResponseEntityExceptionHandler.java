package com.bartosztanski.BlogApp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bartosztanski.BlogApp.entity.ErrorMessage;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(PostNotFoundExcepction.class)
	public ResponseEntity<ErrorMessage> postNotPoundException(PostNotFoundExcepction postNotFoundExcepction, 
																					WebRequest webRequest) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,postNotFoundExcepction.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	
	@ExceptionHandler(PostInsertFailedException.class)
	public ResponseEntity<ErrorMessage> postNotPoundException(PostInsertFailedException postInsertFailedException, 
																					WebRequest webRequest) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST,postInsertFailedException.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	@ExceptionHandler(VideoNotFoundException.class)
	public ResponseEntity<ErrorMessage> VideoNotFoundException(VideoNotFoundException videoNotFoundException, 
																					WebRequest webRequest) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,videoNotFoundException.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
}
