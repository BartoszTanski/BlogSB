package com.bartosztanski.BlogApp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.model.PostsPage;
import com.bartosztanski.BlogApp.model.PostsPageResponse;
import com.bartosztanski.BlogApp.service.PostService;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","http://localhost:3000"})
@RequestMapping("/api/v1")

public class PostPageController {

		private final PostService postService;
		
		public PostPageController(PostService postService) {
			this.postService = postService;
		}
		
		private final Logger LOGGER = LoggerFactory.getLogger(PostPageController.class); 
		
		//Getting all posts one by one
		@GetMapping("/posts/page/{lp}")
		public ResponseEntity<PostsPageResponse> getlAllPostsByPage(@PathVariable("lp")int lp) {
			LOGGER.info("Inside PostPageController.getAllPosts page:{}",lp);
			PostsPage page = postService.getlAllPostsByPage(lp);
			List<PostResponse> posts = page.getPosts()
					.stream().map(e -> e.entityToResponse()).collect(Collectors.toList());
			
			PostsPageResponse pageResponse = PostsPageResponse.builder()
					.posts(posts)
					.size(page.getSize())
					.build();
			return ResponseEntity.ok().body(pageResponse);
		}
}







