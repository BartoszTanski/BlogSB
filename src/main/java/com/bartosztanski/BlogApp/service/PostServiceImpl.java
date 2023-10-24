package com.bartosztanski.BlogApp.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostInsertFailedException;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.model.PostsPage;
import com.bartosztanski.BlogApp.repository.PostRepository;
import com.bartosztanski.BlogApp.utils.ImageResizeService;


@Service
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepository;
	MongoTemplate mongoTemplate;
	ImageResizeService imageResizeService;
	
	public PostServiceImpl (PostRepository postRepository,MongoTemplate mongoTemplate, ImageResizeService imageResizeService) {
		this.postRepository = postRepository;
		this.mongoTemplate = mongoTemplate;
		this.imageResizeService = imageResizeService;
	}

	@Override
	public String addPost(PostRequest postRequest) throws PostInsertFailedException, IOException{
		
		PostEntity postEntity = PostEntity.builder()
										  .title(postRequest.getTitle())
										  .author(postRequest.getAuthor())
										  .description(postRequest.getDescription())
										  .content(postRequest.getContent())
										  .tags(postRequest.getTags())
										  .profilePic(postRequest.getProfilePic())
										  .image(imageResizeService.resizeImage(postRequest.getImage(),1280,720))
										  .time(postRequest.getTime())
										  .video(postRequest.getVideo())
										  .email(postRequest.getEmail())
										  .comments(null)
										  .likes(0)
										  .build();
		
		return postRepository.insert(postEntity).getId();
	}

	@Override
	public void updatePost(String id, PostRequest postRequest) throws PostNotFoundExcepction {

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update updateQuery = new Update(); 
		
		updateQuery.set("title", postRequest.getTitle());
		updateQuery.set("author", postRequest.getAuthor());
		updateQuery.set("description", postRequest.getDescription());
		updateQuery.set("content", postRequest.getContent());
		updateQuery.set("tags", postRequest.getTags());
		if(postRequest.getImage()!=null) {updateQuery.set("image", postRequest.getImage());}
		if((postRequest.getVideo()!=null) && (postRequest.getVideo()!="null")) {updateQuery.set("video", postRequest.getVideo());}
		updateQuery.set("profilePic", postRequest.getProfilePic());
		updateQuery.set("email", postRequest.getEmail());
		updateQuery.set("time", postRequest.getTime());
		
		if(mongoTemplate.findAndModify(query, updateQuery, PostEntity.class)==null)
			throw new PostNotFoundExcepction("Post with id: "+id+ " not found.");
	}

	@Override
	public List<PostEntity> getlAllPosts() {
		return postRepository.getAllPosts( Sort.by(Sort.Direction.DESC, "time"));		
	}

	@Override
	public List<PostEntity> getPostsByDate(int days) {
		LocalDateTime date = LocalDateTime.now().minusDays(days);
		Date out = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		return postRepository.findByDate(out)
				.orElseThrow(()->new RuntimeException(
						String.format("Cannot Find Post added in last %s days", days)));
		
	}

	@Override
	public void deleteById(String id) {
	postRepository.deleteById(id);
	}

	@Override
	public PostResponse getPostById(String id) throws PostNotFoundExcepction {
		
		Optional<PostEntity> post = postRepository.findById(id);
		
		if(!post.isPresent()) throw new PostNotFoundExcepction("Post with id: "+id+" Not Found");

		PostResponse postResponse = post.get().entityToResponse();
		return postResponse;
	}

	@Override
	public byte[] getImage(String id) throws PostNotFoundExcepction {
		
		Optional<PostEntity> post = postRepository.findImageById(id);
		
		if(!post.isPresent()) throw new PostNotFoundExcepction("Post with id: "+id+" Not Found");
		
		byte[] image = post.get().getImage().getData();
		return image;
	}

	@Override
	public List<PostEntity> getTopPosts(int page, int limit, int days) {
		
		LocalDateTime date = LocalDateTime.now().minusDays(days);
		Date isoDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		
		Page<PostEntity> pages = postRepository.findAllExcludeContent(isoDate, 
                PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "likes")));
		
		List<PostEntity> topPosts = pages.getContent();
		
		return topPosts;
	}

	@Override
	public void updateLikes(String postId, int i) throws PostNotFoundExcepction {
		
		Query query=new Query(Criteria.where("id").is(postId));
		PostEntity post=mongoTemplate.findOne(query,PostEntity.class);

		if(post!=null){
		   Update update=new Update().inc("likes",i);
		   mongoTemplate.updateFirst(query,update,PostEntity.class);
		}
		else throw new PostNotFoundExcepction("Post with id: "+postId+" Not Found");
	}

	@Override
	public List<PostEntity> getPostsByTag(String tagId) {
		List<PostEntity> posts = postRepository.findAllByTag(tagId, Sort.by(Sort.Direction.DESC, "time"));
		return posts;
	}

	@Override
	public List<PostEntity> findPostByRegexpTitle(String regexp) {
		List<PostEntity> posts = postRepository.findPostByRegexpTitle(regexp);
		return posts;
	}

	@Override
	public PostsPage getlAllPostsByPage(int lp, int size) {
		Pageable pageable = PageRequest.of(lp, size, Sort.by("time").descending());
		Page<PostEntity> pages = postRepository.findAll(pageable);
		List<PostEntity> posts = pages.getContent();
		int _size = pages.getTotalPages();
		PostsPage page = PostsPage.builder()
				.posts(posts)
				.size(_size)
				.build();
		return page;
	}
	
	
	
}