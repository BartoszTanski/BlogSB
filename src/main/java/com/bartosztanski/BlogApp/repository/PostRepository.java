package com.bartosztanski.BlogApp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.Binary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bartosztanski.BlogApp.entity.CommentEntity;
import com.bartosztanski.BlogApp.entity.PostEntity;

@Repository
public interface PostRepository extends MongoRepository<PostEntity, String> {
	@Query("{'date': ?0}")
	Optional<List<PostEntity>> findByDate(LocalDate date);
	@Query("{'title': ?0}")
	Optional<List<PostEntity>> findByTitle(LocalDate date);
	//@Query()
	//void addComment(CommentEntity commentEntity) {} //TO DO
	@Query("{'id': ?0}")
	Optional<PostEntity> findById(String id);
	@Query(value = "{'id': ?0}", fields = "{ 'image' : 1}")
	PostEntity findImageById(String id);
	@Query(value = "{'id': ?0}", fields = "{ 'comments' : 1}")
	PostEntity getCommentsByPostId(String postId);
}
