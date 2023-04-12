package com.bartosztanski.BlogApp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.model.PostsResponse;
import com.bartosztanski.BlogApp.service.PostService;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","http://localhost:3000"})
@RequestMapping("/api/v1")
public class PostController {

	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
	//Adding post 
	@PostMapping("/posts")
	public ResponseEntity<String> addPost(@RequestParam("title") String title, 
			@RequestParam("description") String description,
			@RequestParam("author") String author,@RequestParam("content") String content,
			@RequestParam("tags") String tags,@RequestParam("file") MultipartFile file,
			@RequestParam("profilePic") String profilePic) throws IOException {
		LOGGER.info("Inside PostController.addPost");
		PostRequest postRequest = PostRequest.builder()
											 .title(title)
											 .description(description)
											 .author(author)
											 .content(content)
											 .tags(tags.split(","))
											 .image(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
											 .profilePic(profilePic)
											 .time(LocalDateTime.now())
											 .likes(0)
											 .build();
		String id = postService.addPost(postRequest);
		return new ResponseEntity<>("Post: "+id+ " added succesfully ", HttpStatus.CREATED);
	} 
	
	@PutMapping("/posts")
	public ResponseEntity<PostEntity> updatePost(@RequestParam("id") String id,@RequestParam("title") String title, 
			@RequestParam("description") String description,
			@RequestParam("author") String author,@RequestParam("content") String content,
			@RequestParam("tags") String tags,@RequestParam("file") MultipartFile file,
			@RequestParam("profilePic") String profilePic) throws IOException {
		PostRequest postRequest = PostRequest.builder()
											 .title(title)
											 .description(description)
											 .author(author)
											 .content(content)
											 .tags(tags.split(","))
											 .image(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
											 .profilePic(profilePic)
											 .time(LocalDateTime.now())
											 .build();
		LOGGER.info("Inside PostController.updatePost");
		postService.updatePost(id, postRequest);
		return ResponseEntity.ok().build(); 
	}
	@GetMapping("/posts")
	public ResponseEntity<List<PostsResponse>> getlAllPosts() {
		LOGGER.info("Inside PostController.getAllPosts");
		List<PostsResponse> posts = postService.getlAllPosts()
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		return ResponseEntity.ok(posts);
	}
	@GetMapping("/posts/date/{stringDate}")
	public ResponseEntity<List<PostEntity>> getPostsByDate(@PathVariable("stringDate") String stringDate) {
		LOGGER.info("Inside PostController.getPostByDate");
		LocalDate date = LocalDate.parse(stringDate);
		return ResponseEntity.ok(postService.getPostsByDate(date));
	}
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") String id) {
		LOGGER.info("Inside PostController.deletePost");
		postService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@GetMapping("/posts/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable("id") String id) throws PostNotFoundExcepction {
		LOGGER.info("Inside PostController.getPostById");
		PostResponse post = postService.getPostById(id);
		return ResponseEntity.ok(post);
	}
	
	@GetMapping("/posts/top")
	public ResponseEntity<List<PostsResponse>> getTopPosts() {
		LOGGER.info("Inside PostController.getTopPosts");
		int page = 0;
		int limit = 5; 
		List<PostsResponse> topPosts = postService.getTopPosts(page, limit)
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		return ResponseEntity.ok(topPosts);
		
	}
	@GetMapping(value="/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
		LOGGER.info("Inside PostController.getImage");
		byte[] image = postService.getImage(id);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
	
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		
		return ResponseEntity.ok("hello");
	}
	
	@PutMapping("/posts/{postId}/like")
	public ResponseEntity<String> incrementLikes(@PathVariable("postId")String postId){
		LOGGER.info("Inside PostController.incrementLikes");
		postService.updateLikes(postId,1);
		return ResponseEntity.ok("Updated likes - incremented");
	}
	@PutMapping("/posts/{postId}/unlike")
	public ResponseEntity<String> decrenentLikes(@PathVariable("postId")String postId){
		LOGGER.info("Inside PostController.decrenentLikes");
		postService.updateLikes(postId,-1);
		return ResponseEntity.ok("Updated likes - decremented");
	}
	
	@GetMapping("/posts/tag/{tagId}")
	public ResponseEntity<List<PostsResponse>> getPostsByTag(@PathVariable("tagId") String tagId) {
		LOGGER.info("Inside PostController.getPostsByTag");
		List<PostsResponse> posts = postService.getPostsByTag(tagId)
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		return ResponseEntity.ok(posts);
	}
}


