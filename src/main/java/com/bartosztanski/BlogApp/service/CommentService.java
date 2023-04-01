package com.bartosztanski.BlogApp.service;

import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.model.CommentRequest;

@Service
public interface CommentService {
	public String addComment(CommentRequest commentRequest);
	public void editComment(CommentRequest commentRequest);
	public void deleteComment(String postId, String  commentId);
}
