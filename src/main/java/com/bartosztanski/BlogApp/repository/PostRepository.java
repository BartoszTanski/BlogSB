package com.bartosztanski.BlogApp.repository;

import java.time.LocalDate;
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
	@Query("{'date': ?0}")
	Optional<List<PostEntity>> findByDate(LocalDate date);
	@Query("{'title': ?0}")
	Optional<List<PostEntity>> findByTitle(LocalDate date);
	@Query("{'id': ?0}")
	Optional<PostEntity> findById(String id);
	@Query(value = "{'id': ?0}", fields = "{ 'image' : 1}")
	PostEntity findImageById(String id);
	@Query(value = "{'id': ?0}", fields = "{ 'comments' : 1}")
	PostEntity getCommentsByPostId(String postId);
	@Query(value = "{}",fields = "{'comments' : 0, 'content' : 0, 'author' : 0, 'profilePic' : 0, 'description' :0}")
	Page<PostEntity> findAllExcludeContent(Pageable pageable);
	@Query(value = "{'tags': ?0}",fields = "{'comments' : 0, 'content' : 0}")
	List<PostEntity> findAllByTag(String tagId);
	@Query(value="{ 'title' : { $regex: ?0 } }",fields = "{'comments' : 0, 'content' : 0, 'author' : 0, 'profilePic' : 0, 'description' :0}")
	List<PostEntity> findPostByRegexpTitle(String regexp);
}
