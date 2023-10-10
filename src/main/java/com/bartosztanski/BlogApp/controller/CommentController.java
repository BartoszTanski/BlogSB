package com.bartosztanski.BlogApp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bartosztanski.BlogApp.model.CommentRequest;
import com.bartosztanski.BlogApp.service.CommentService;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","http://localhost:3000"})
@RequestMapping("api/v1/post")
public class CommentController {

	CommentService commentService;
	
	public CommentController (CommentService commentService) {
		this.commentService = commentService;
	}
	
	private final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

	@PostMapping("/{postId}/comments")
	public ResponseEntity<String> addComment(@PathVariable("postId") String postId,
			@RequestParam("author") String author,
			@RequestParam("content") String content, 
			@RequestParam("profilePic") String profilePic) throws IOException {
		
		LOGGER.info("Inside CommentController.addComment");
		
		CommentRequest commentRequest = CommentRequest.builder()
													  .author(author)
													  .content(content)
													  .postId(postId)
													  .profilePic(profilePic)
													  .build();

		String id = commentService.addComment(commentRequest);
		return new ResponseEntity<>(id+" succesfully ", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{postId}/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId) {
		
		LOGGER.info("Inside CommentController.deleteComment");
		
		commentService.deleteComment(postId, commentId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
