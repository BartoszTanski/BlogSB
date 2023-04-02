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
@CrossOrigin(origins = "https://bartosztanski.azurewebsites.net/")
@RequestMapping("/api/v1")
public class PostController {

	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
	
	@PostMapping("/posts")
	public ResponseEntity<String> addPost(@RequestParam("title") String title, 
			@RequestParam("description") String description,
			@RequestParam("author") String author ,@RequestParam("content") String content,
			@RequestParam("tags") String tags, @RequestParam("file") MultipartFile file,
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
											 .build();
		String id = postService.addPost(postRequest);
		return new ResponseEntity<>("Post: "+id+ " added succesfully ", HttpStatus.CREATED);
	} 
	
	@PutMapping("/posts")
	public ResponseEntity<PostEntity> updatePost(@RequestBody PostEntity postEntity) {
		LOGGER.info("Inside PostController.updatePost");
		postService.updatePost(postEntity);
		return ResponseEntity.ok().build(); 
	}
	@GetMapping("/posts")
	public ResponseEntity<List<PostsResponse>> getlAllPosts() {
		LOGGER.info("Inside PostController.getAllPosts");
		List<PostsResponse> posts = postService.getlAllPosts()
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		return ResponseEntity.ok(posts);
	}
	@GetMapping("/posts/{stringDate}")
	public ResponseEntity<List<PostEntity>> getPostsByDate(@PathVariable("stringDate") String stringDate) {
		LOGGER.info("Inside PostController.getPostByDate");
		LocalDate date = LocalDate.parse(stringDate);
		return ResponseEntity.ok(postService.getPostsByDate(date));
	}
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<PostEntity> deletePost(@PathVariable("id") String id) {
		LOGGER.info("Inside PostController.deletePost");
		postService.deletePostById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@GetMapping("/post/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable("id") String id) throws PostNotFoundExcepction {
		LOGGER.info("Inside PostController.getPostById");
		PostResponse post = postService.getPostById(id);
		return ResponseEntity.ok(post);
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
}


