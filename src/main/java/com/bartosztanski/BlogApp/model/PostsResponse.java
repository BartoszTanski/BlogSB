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
public class PostsResponse {
	
	private String id;
	private String title;
	private String description;
	private LocalDateTime time;
	private String author;
	private String profilePic;
	private String image; 
	private String[] tags;
	private int likes;
}