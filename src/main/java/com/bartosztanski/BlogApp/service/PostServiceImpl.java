package com.bartosztanski.BlogApp.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.repository.PostRepository;


@Service
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepository;
	
	public PostServiceImpl (PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public String addPost(PostRequest postRequest) {
		
		PostEntity postEntity = PostEntity.builder()
										  .title(postRequest.getTitle())
										  .author(postRequest.getAuthor())
										  .description(postRequest.getDescription())
										  .content(postRequest.getContent())
										  .tags(postRequest.getTags())
										  .profilePic(postRequest.getProfilePic())
										  .image(postRequest.getImage())
										  .time(postRequest.getTime())
										  .comments(null)
										  .build();
		
		return postRepository.insert(postEntity).getId();
	}

	@Override
	public void updatePost(PostEntity postEntity) {
		PostEntity savedPost = postRepository.findById(postEntity.getId())
				.orElseThrow(()->new RuntimeException(
						String.format("Cannot Find Post by ID %s", postEntity.getId())));
		savedPost.setTime(postEntity.getTime());
		savedPost.setContent(postEntity.getContent());
		savedPost.setTitle(postEntity.getTitle());
		
		postRepository.save(savedPost);
		
	}

	@Override
	public List<PostEntity> getlAllPosts() {
		return postRepository.findAll();		
	}

	@Override
	public List<PostEntity> getPostsByDate(LocalDate date) {
		return postRepository.findByDate(date)
				.orElseThrow(()->new RuntimeException(
						String.format("Cannot Find Post by date %s", date)));
		
	}

	@Override
	public void deletePostById(String id) {
		postRepository.deleteById(id);
		
	}

	@Override
	public PostResponse getPostById(String id) throws PostNotFoundExcepction {
		Optional<PostEntity> optionalPost = postRepository.findById(id);
		
		if(!optionalPost.isPresent()) {
			throw new PostNotFoundExcepction("Post Not Found");
		}
		
		PostEntity post = optionalPost.get();
		PostResponse postResponse = PostResponse.builder()
												.id(post.getId())
												.title(post.getTitle())
												.author(post.getAuthor())
												.description(post.getDescription())
												.content(post.getContent())
												.time(post.getTime())
												.image("data:image/png;base64,"+
												Base64.getEncoder().encodeToString(post.getImage().getData()))
												.profilePic(post.getProfilePic())
												.comments(post.getComments() != null? post.getComments().stream()
														   .map(com -> com.entityToResponse())
														   .collect(Collectors.toList())
															:null)
												.tags(post.getTags())
												.build();
		return postResponse;
	}

	@Override
	public byte[] getImage(String id) {
		PostEntity post = postRepository.findImageById(id);
		byte[] image = post.getImage().getData();
		return image;
	}
	
}