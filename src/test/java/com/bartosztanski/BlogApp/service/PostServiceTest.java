package com.bartosztanski.BlogApp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.Optional;

import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostInsertFailedException;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

	@Autowired
	private PostService postService;
	
	@MockBean
	PostRepository postRepository;
	
	@BeforeEach
	void setUp() {	
		PostEntity postEntity = PostEntity.builder()
				  .id("6423bfcbb42e9c615a2670e0")
				  .title("test Title")
				  .author("test Author")
				  .description("test Description")
				  .content("test Content")
				  .tags(new String[]{"tag1","tag2","tag3"})
				  .profilePic("test ProfilePic Url")
				  .image(new Binary("test".getBytes()))
				  .time(LocalDateTime.of(2019, 03, 28, 14, 33))
				  .comments(null)
				  .build();
		
		Mockito.when(postRepository.insert(any(PostEntity.class))).thenReturn(postEntity);
		Mockito.when(postRepository.findById("6423bfcbb42e9c615a2670e0")).thenReturn(Optional.of(postEntity));
	}

	@Test
	@DisplayName("Add new post if valid request")
	public void ShouldReturnPostEntityIdIfPostRequestValid() throws PostInsertFailedException , Exception {
		
		PostRequest postRequest = 
				PostRequest.builder()
				.title("test Title")
				.author("test Author")
				.description("test Description")
				.profilePic("test ProfilePic Url")
				.content("test Content")
				.image(new Binary("test".getBytes()))
				.tags(new String[]{"tag1","tag2","tag3"})
				.time(LocalDateTime.of(2019, 03, 28, 14, 33))
				.build();

		String id ="6423bfcbb42e9c615a2670e0";
		String addedPostId = postService.addPost(postRequest); 
		assertEquals(id, addedPostId);
	}
	
	@Test
	@DisplayName("Get post if post id found")
	public void ShouldReturnPostEntityIfPostIdFound() throws PostNotFoundExcepction {
		
		String id ="6423bfcbb42e9c615a2670e0";
		
		PostResponse postResponse = PostResponse.builder()
				  .id("6423bfcbb42e9c615a2670e0")
				  .title("test Title")
				  .author("test Author")
				  .description("test Description")
				  .content("test Content")
				  .tags(new String[]{"tag1","tag2","tag3"})
				  .profilePic("test ProfilePic Url")
				  .image("data:image/png;base64,dGVzdA==")
				  .time(LocalDateTime.of(2019, 03, 28, 14, 33))
				  .comments(null)
				  .build();
		
		PostResponse foundPost = postService.getPostById(id); 
		assertEquals(postResponse, foundPost);
	}
	
	@Test
	@DisplayName("Throw exception if post id not found")
	public void ShouldThrowExceptionIfPostNotFound() throws PostNotFoundExcepction {
		
		String id ="6423bfcbb42e9c615a2670e9";
		Exception exception = assertThrows(PostNotFoundExcepction.class, () ->
		postService.getPostById(id));
    assertEquals("Post Not Found", exception.getMessage());
	}

}
