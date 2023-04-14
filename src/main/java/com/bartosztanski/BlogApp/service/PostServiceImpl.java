package com.bartosztanski.BlogApp.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.model.PostsResponse;
import com.bartosztanski.BlogApp.repository.PostRepository;


@Service
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepository;
	MongoTemplate mongoTemplate;
	
	public PostServiceImpl (PostRepository postRepository,MongoTemplate mongoTemplate) {
		this.postRepository = postRepository;
		this.mongoTemplate = mongoTemplate;
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
										  .likes(0)
										  .build();
		
		return postRepository.insert(postEntity).getId();
	}

	@Override
	public void updatePost(String id, PostRequest postRequest) {

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update updateQuery = new Update(); 
		
		updateQuery.set("title", postRequest.getTitle());
		updateQuery.set("author", postRequest.getAuthor());
		updateQuery.set("description", postRequest.getDescription());
		updateQuery.set("content", postRequest.getContent());
		updateQuery.set("tags", postRequest.getTags());
		if(postRequest.getImage()!=null) {updateQuery.set("image", postRequest.getImage());}
		updateQuery.set("profilePic", postRequest.getProfilePic());
		updateQuery.set("time", postRequest.getTime());
		
		mongoTemplate.findAndModify(query, updateQuery, PostEntity.class);
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
	public void deleteById(String id) {
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
												.likes(post.getLikes())
												.build();
		return postResponse;
	}

	@Override
	public byte[] getImage(String id) {
		PostEntity post = postRepository.findImageById(id);
		byte[] image = post.getImage().getData();
		return image;
	}

	@Override
	public List<PostEntity> getTopPosts(int page, int limit) {
		Page<PostEntity> pages = postRepository.findAllExcludeContent( 
                PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "likes")));
		List<PostEntity> topPosts = pages.getContent();
		
		return topPosts;
	}

	@Override
	public void updateLikes(String postId, int i) {
		
		Query query=new Query(Criteria.where("id").is(postId));
		PostEntity post=mongoTemplate.findOne(query,PostEntity.class);

		if(post!=null){
		   Update update=new Update().inc("likes",i);
		   mongoTemplate.updateFirst(query,update,PostEntity.class);
		}	    
	}

	@Override
	public List<PostEntity> getPostsByTag(String tagId) {
		List<PostEntity> posts = postRepository.findAllByTag(tagId);
		return posts;
	}

	@Override
	public List<PostEntity> findPostByRegexpTitle(String regexp) {
		List<PostEntity> posts = postRepository.findPostByRegexpTitle(regexp);
		return posts;
	}
	
	
	
}