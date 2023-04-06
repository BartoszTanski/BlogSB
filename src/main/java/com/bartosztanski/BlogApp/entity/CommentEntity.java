package com.bartosztanski.BlogApp.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.bartosztanski.BlogApp.model.CommentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {
	@Id
	private String id;
	private String author;
	private String profilePic;
	private String content;
	private LocalDateTime time;


	public static class CommentEntityBuilder {
		public CommentEntityBuilder id() {
			this.id = new ObjectId().toString();
			return this;
		}
	}
	
	public CommentResponse entityToResponse() {
		CommentResponse commentResponse = CommentResponse.builder()
										.id(id)	
										.content(content)
										.author(author)
										.profilePic(profilePic)
										.time(time)
										.build();
		return commentResponse;
	}
}