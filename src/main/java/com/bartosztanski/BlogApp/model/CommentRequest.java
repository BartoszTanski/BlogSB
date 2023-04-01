package com.bartosztanski.BlogApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
	private String author;
	private String profilePic;
	private String content;
	private String postId;
}

