package com.bartosztanski.BlogApp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bartosztanski.BlogApp.entity.PostEntity;

@Repository
public interface PostRepository extends MongoRepository<PostEntity, String> {
	
	//Find POSTS newer than TIME
	@Query(value="{'time' : { $gt : ?0} }")
	Optional<List<PostEntity>> findByDate(Date date);
	//Find POST by postID
	@Query("{'id': ?0}")
	Optional<PostEntity> findById(String id);
	//Find IMAGE by postID
	@Query(value = "{'id': ?0}", fields = "{ 'image' : 1}")
	PostEntity findImageById(String id);
	//Get COMMENTS by postID
	@Query(value = "{'id': ?0}", fields = "{ 'comments' : 1}")
	PostEntity getCommentsByPostId(String postId);
	//Find all POSTS without some fields
	@Query(value = "{'time' : { $gt : ?0} }",fields = "{'comments' : 0, 'content' : 0, 'author' : 0, 'profilePic' : 0, 'description' :0}")
	Page<PostEntity> findAllExcludeContent(Date date,Pageable pageable);
	//Find POSTS by TAG
	@Query(value = "{'tags': ?0}",fields = "{'comments' : 0, 'content' : 0}")
	List<PostEntity> findAllByTag(String tagId);
	//Find POSTS by TITLE REGEX
	@Query(value="{ 'title' : { $regex: ?0 } }",fields = "{'comments' : 0, 'content' : 0, 'author' : 0, 'profilePic' : 0, 'description' :0}")
	List<PostEntity> findPostByRegexpTitle(String regexp);
	//Find all POSTS without some fields
	@Query(value = "{}",fields = "{'comments' : 0, 'content' : 0}")
	List<PostEntity> getAllPosts();
}
