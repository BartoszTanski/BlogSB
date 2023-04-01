package com.bartosztanski.BlogApp.entity;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bartosztanski.BlogApp.model.PostsResponse;

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
	private Binary image; //BSON Mongodb type
	private String[] tags;
	private List<CommentEntity> comments;
	
	public PostsResponse entityToResponse() {
		PostsResponse postResponse = PostsResponse.builder()
										.id(id)
										.title(title)
										.author(author)
										.description(description)
										.image("data:image/png;base64,"+Base64.getEncoder().encodeToString(image.getData()))
										.profilePic(profilePic)
										.time(time)
										.tags(tags)
										.build();
		return postResponse;
	}
}

