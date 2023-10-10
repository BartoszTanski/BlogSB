package com.bartosztanski.BlogApp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bartosztanski.BlogApp.entity.PostEntity;

@Repository
public interface PostRepository extends MongoRepository<PostEntity, String> {
	

	@Query(value="{'time' : { $gt : ?0} }")
	Optional<List<PostEntity>> findByDate(Date date);

	@Query("{'id': ?0}")
	Optional<PostEntity> findById(String id);

	@Query(value = "{'id': ?0}", fields = "{ 'image' : 1}")
	Optional<PostEntity> findImageById(String id);

	@Query(value = "{'id': ?0}", fields = "{ 'comments' : 1}")
	PostEntity getCommentsByPostId(String postId);

	@Query(value = "{'time' : { $gt : ?0} }",
			fields = "{'comments' : 0, 'content' : 0, 'author' : 0, 'profilePic' : 0, 'description' :0}")
	Page<PostEntity> findAllExcludeContent(Date date,Pageable pageable);

	@Query(value = "{'tags': ?0}",fields = "{'comments' : 0, 'content' : 0}")
	List<PostEntity> findAllByTag(String tagId, Sort sort);

	@Query(value="{ 'title' : { $regex: ?0, $options: 'i'} }",
			fields = "{'comments' : 0, 'content' : 0, 'profilePic' : 0,}")
	List<PostEntity> findPostByRegexpTitle(String regexp);

	@Query(value = "{}",fields = "{}")
	List<PostEntity> getAllPosts(Sort sort);

	Page<PostEntity> findAll(Pageable pageable);
}
