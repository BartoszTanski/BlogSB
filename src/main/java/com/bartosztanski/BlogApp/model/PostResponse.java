package com.bartosztanski.BlogApp.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
	private String id;
	private String title;
	private String description;
	private String content;
	private LocalDateTime time;
	private String author;
	private String profilePic;
	private String image; 
	private String[] tags;
	private String video;
	private String email;
	private List<CommentResponse> comments;
	private int likes;
}