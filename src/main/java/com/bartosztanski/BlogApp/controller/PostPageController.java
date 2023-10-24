package com.bartosztanski.BlogApp.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bartosztanski.BlogApp.error.InvalidPageNumberException;
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
		
		@GetMapping("/posts/page/{lp}")
		public ResponseEntity<PostsPageResponse> getlAllPostsByPage(
				@PathVariable("lp")int lp,
				@RequestParam("size")Optional<Integer> size) throws InvalidPageNumberException {
			
			int _size = size.orElse(3);
			if (lp < 0) throw new InvalidPageNumberException("Page number cannot be less than zero!, send: "+lp);
			if (_size < 0) throw new InvalidPageNumberException("Page size cannot be less than zero!, send: "+_size);
			
			PostsPage page = postService.getlAllPostsByPage(lp,_size);
			
			List<PostResponse> posts = page.getPosts()
					.stream()
					.map(e -> e.entityToResponse())
					.collect(Collectors.toList());
			
			PostsPageResponse pageResponse = PostsPageResponse.builder()
					.posts(posts)
					.size(page.getSize())
					.build();
			
			CacheControl cacheControl = CacheControl.maxAge(10, TimeUnit.SECONDS)
				    .noTransform()
				    .mustRevalidate();
			
			LOGGER.info("Returned "+posts.size()+" posts by page where page: "+lp);
			return ResponseEntity.ok().cacheControl(cacheControl).body(pageResponse);
		}
}







