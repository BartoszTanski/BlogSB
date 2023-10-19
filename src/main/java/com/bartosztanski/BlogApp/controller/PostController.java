package com.bartosztanski.BlogApp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostInsertFailedException;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
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
	
	@PostMapping("/posts")
	public ResponseEntity<String> addPost(
			@RequestParam("title") String title, 
			@RequestParam("description") String description,
			@RequestParam("author") String author,
			@RequestParam("content") String content,
			@RequestParam("tags") String tags,
			@RequestParam("image") MultipartFile image,
			@RequestParam("profilePic") String profilePic,
			@RequestParam("video") String video,
			@RequestParam("email") String email) throws PostInsertFailedException, IOException {
		
		PostRequest postRequest = PostRequest.builder()
											 .title(title)
											 .description(description)
											 .author(author)
											 .content(content)
											 .tags(tags.split(","))
											 .image(image!=null? new Binary(BsonBinarySubType.BINARY, image.getBytes()):null)
											 .profilePic(profilePic)
											 .time(LocalDateTime.now())
											 .likes(0)
											 .video(video)
											 .email(email)
											 .build();
		
		String id = postService.addPost(postRequest);
		LOGGER.info("Added new post: "+id+", with title: "+title+", made by: "+author);
		return new ResponseEntity<>("Post: "+id+ " added succesfully ", HttpStatus.CREATED);
	} 
	
	//Updating post by ID
	@PutMapping("/posts")
	public ResponseEntity<PostEntity> updatePost(@RequestParam("id") String id,@RequestParam("title") String title, 
			@RequestParam("description") String description,
			@RequestParam("author") String author,@RequestParam("content") String content,
			@RequestParam("tags") String tags,@RequestParam("file") Optional<MultipartFile> file,
			@RequestParam("profilePic") String profilePic,
			@RequestParam("video") String video,
			@RequestParam("email") String email) throws IOException, PostNotFoundExcepction {
		
		PostRequest postRequest = PostRequest.builder()
											 .title(title)
											 .description(description)
											 .author(author)
											 .content(content)
											 .tags(tags.split(","))
											 .image(file.isPresent()?new Binary(
													 BsonBinarySubType.BINARY, 
													 file.get().getBytes()):null)
											 .profilePic(profilePic)
											 .time(LocalDateTime.now())
											 .video(video)
											 .email(email)
											 .build();
		
		postService.updatePost(id, postRequest);
		LOGGER.info("Updated post: "+id+", made by: "+author);
		return ResponseEntity.ok().build(); 
	}
	
	//Getting all posts
	@GetMapping("/posts")
	public ResponseEntity<List<PostResponse>> getlAllPosts() {
		
		List<PostResponse> posts = postService.getlAllPosts()
				.stream()
				.map(e -> e.entityToResponse())
				.collect(Collectors.toList());
		
		CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
			      .noTransform()
			      .mustRevalidate();
		
		LOGGER.info("Retured all posts");
		return ResponseEntity.ok().cacheControl(cacheControl).body(posts);
	}
	
	//Getting post by days interval
	@GetMapping("/posts/days/{days}")
	public ResponseEntity<List<PostEntity>> getPostsByDate(@PathVariable("days") int days) {
		
		LOGGER.info("Retured all posts made no longer than "+days+" days ago");
		return ResponseEntity.ok(postService.getPostsByDate(days));
	}
	
	//Deleting post by ID
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") String id) throws PostNotFoundExcepction {
		
		postService.deleteById(id);
		LOGGER.info("Deleted post with id: "+id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	//Getting post by ID
	@GetMapping("/posts/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable("id") String id) throws PostNotFoundExcepction {
		
		PostResponse post = postService.getPostById(id);
		
		CacheControl cacheControl = CacheControl.maxAge(10, TimeUnit.SECONDS)
			      .noTransform()
			      .mustRevalidate();
		
		LOGGER.info("Returned post with id: "+id);
		return ResponseEntity.ok().cacheControl(cacheControl).body(post);
	}
	
	//Getting 'limit' posts with most likes in last 'range' days
	@GetMapping("/posts/top")
	public ResponseEntity<List<PostResponse>> getTopPosts(@RequestParam("page") Optional<Integer> page,
			@RequestParam("limit") Optional<Integer> limit, @RequestParam("range") Optional<Integer> range) {

		int pageNumber = page.orElseGet(() -> 0);
		int pageSize = limit.orElseGet(() -> 5);
		int daysRange = range.orElseGet(() -> 7);
		List<PostResponse> topPosts = postService.getTopPosts(pageNumber, pageSize, daysRange)
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		
		CacheControl cacheControl = CacheControl.maxAge(120, TimeUnit.SECONDS)
			      .noTransform();
		
		LOGGER.info("Returned page: "+pageNumber+", with "+pageSize+" posts with most likes in the last "+daysRange+" days");
		return ResponseEntity.ok().cacheControl(cacheControl).body(topPosts);	
	}
	
	//Getting main image by post ID
	@GetMapping(value="/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) throws PostNotFoundExcepction {
		
		byte[] image = postService.getImage(id);
		
		CacheControl cacheControl = CacheControl.maxAge(2419200, TimeUnit.SECONDS); //28 days
		
		LOGGER.info("Returned image with id: "+id);
		return ResponseEntity.ok().cacheControl(cacheControl).contentType(MediaType.IMAGE_PNG).body(image);
	}
	
	//Updating post likes +1 TODO add param. instead 2 controllers
	@PutMapping("/posts/{postId}/like")
	public ResponseEntity<String> incrementLikes(@PathVariable("postId")String postId) throws PostNotFoundExcepction {
		
		postService.updateLikes(postId,1);
		LOGGER.info("Incremented likes to post: "+postId+" by 1");
		return ResponseEntity.ok("Updated likes - incremented");
	}
	
	//Updating post likes -1 TODO add param. instead 2 controllers
	@PutMapping("/posts/{postId}/unlike")
	public ResponseEntity<String> decrenentLikes(@PathVariable("postId")String postId) throws PostNotFoundExcepction {
		
		postService.updateLikes(postId,-1);
		LOGGER.info("Decremented likes to post: "+postId+" by 1");
		return ResponseEntity.ok("Updated likes - decremented");
	}
	
	//Getting posts by TAG
	@GetMapping("/posts/tag/{tagId}")
	public ResponseEntity<List<PostResponse>> getPostsByTag(@PathVariable("tagId") String tagId)  {
		
		List<PostResponse> posts = postService.getPostsByTag(tagId)
				.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
		
		LOGGER.info("Returned all posts with tag: "+tagId);
		return ResponseEntity.ok(posts);
	}
	
	//Getting posts by title REGEX
	@GetMapping("/posts/search/regex")
	public ResponseEntity<List<PostResponse>> getPostsByTitleRegex(@RequestParam("regex") String regex) {
		
		List<PostResponse> posts = postService.findPostByRegexpTitle(regex)
				.stream()
				.map(e -> e.entityToResponse())
				.collect(Collectors.toList());
		
		CacheControl cacheControl = CacheControl.maxAge(120, TimeUnit.SECONDS)
			      .noTransform()
			      .mustRevalidate();
		
		LOGGER.info("Returned "+posts.size()+" posts by regex: "+regex);
		return ResponseEntity.ok().cacheControl(cacheControl).body(posts);
	}	
	
	//API Status
	@GetMapping("/hello")
	public ResponseEntity<String> status() {
		LOGGER.info("Server status called");
		return ResponseEntity.ok("server status: OK");
	}
}


