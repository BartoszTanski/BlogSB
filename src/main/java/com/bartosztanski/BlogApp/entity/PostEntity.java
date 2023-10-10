package com.bartosztanski.BlogApp.entity;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bartosztanski.BlogApp.model.PostResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts")
public class PostEntity {
	
	@Id
	private String id;
	private String title;
	private String description;
	private String content;
	private LocalDateTime time;
	private String author;
	private String profilePic;
	private Binary image; 
	private String[] tags;
	private String video;
	private String email;
	private List<CommentEntity> comments;
	private int likes;
	
	public PostResponse entityToResponse() {
		
		PostResponse postResponse = PostResponse.builder()
					.id(id)
					.title(title)
					.author(author)
					.description(description)
					.content(content)
					.image(image!=null?"data:image/png;base64,"+Base64
										.getEncoder()
										.encodeToString(image.getData())
							:null)
					.profilePic(profilePic)
					.time(time)
					.tags(tags)
					.likes(likes)
					.video(video)
					.email(email)
					.comments(comments != null? comments.stream()
									.map(com -> com.entityToResponse())
									.collect(Collectors.toList())
							 :null)
					.build();
		
		return postResponse;
	}
}

