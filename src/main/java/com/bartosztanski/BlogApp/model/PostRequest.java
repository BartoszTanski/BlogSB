package com.bartosztanski.BlogApp.model;

import java.time.LocalDateTime;

import org.bson.types.Binary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
	
	private String title;
	private String content;
	private String description;
	private LocalDateTime time;
	private String author;
	private String profilePic;
	private Binary image; //BSON Mongodb type
	private String[] tags;
}
