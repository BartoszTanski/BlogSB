package com.bartosztanski.BlogApp.service;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.CommentEntity;
import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.model.CommentRequest;
import com.bartosztanski.BlogApp.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

	PostRepository postRepository;
	MongoTemplate mongoTemplate;
	
	public CommentServiceImpl(PostRepository postRepository, MongoTemplate mongoTemplate) {
		this.postRepository = postRepository;
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public String addComment(CommentRequest commentRequest) {
		String postId = commentRequest.getPostId();
		CommentEntity commentEntity = CommentEntity.builder()
												   .id()
												   .author(commentRequest.getAuthor())
												   .content(commentRequest.getContent())
												   .profilePic(commentRequest.getProfilePic())
												   .time(LocalDateTime.now())
												   .build();

		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(postId));
		Update updateQuery = new Update(); 
		updateQuery.push("comments", commentEntity);
		mongoTemplate.findAndModify(query, updateQuery, PostEntity.class);
		return "Comment added";
	}

	@Override
	public void editComment(CommentRequest commentRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteComment(String PostId, String commentId) {
		// TODO Auto-generated method stub
		
	}

}
