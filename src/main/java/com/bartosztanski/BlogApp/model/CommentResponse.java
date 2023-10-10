package com.bartosztanski.BlogApp.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
	
	private String id;
	private String author;
	private String profilePic; 
	private String content;
	private LocalDateTime time;
}