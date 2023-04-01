package com.bartosztanski.BlogApp.mapping;

import java.util.Base64;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.model.PostsResponse;

public class PostMapping {
	public PostsResponse entityToResponse(PostEntity postEntity) {
		PostsResponse postResponse = PostsResponse.builder()
										.id(postEntity.getId())
										.title(postEntity.getTitle())
										.author(postEntity.getAuthor())
										.description(postEntity.getDescription())
										.image(Base64.getEncoder().encodeToString(postEntity.getImage().getData()))
										.profilePic(postEntity.getProfilePic()) /*Base64.getEncoder().encodeToString(postEntity.getProfilePic().getData()))*/
										.time(postEntity.getTime())
										.tags(postEntity.getTags())
										.build();
		return postResponse;
	}
}
