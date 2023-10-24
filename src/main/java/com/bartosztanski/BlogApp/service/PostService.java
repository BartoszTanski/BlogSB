package com.bartosztanski.BlogApp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostInsertFailedException;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;
import com.bartosztanski.BlogApp.model.PostsPage;

@Service
public interface PostService {
	
	public String addPost(PostRequest postRequest) 
			throws PostInsertFailedException, IOException; 
	
	public void updatePost(String id, PostRequest postRequest)
			throws PostNotFoundExcepction; 
	
	public void deleteById(String id) throws PostNotFoundExcepction;
	public PostResponse getPostById(String id) throws PostNotFoundExcepction;
	public byte[] getImage(String id) throws PostNotFoundExcepction; 
	public void updateLikes(String id, int i) throws PostNotFoundExcepction;
	public List<PostEntity> getlAllPosts();
	public List<PostEntity> getPostsByDate(int days); 
	public List<PostEntity> getTopPosts(int page, int limit, int days);
	public List<PostEntity> getPostsByTag(String tagId);
	public List<PostEntity> findPostByRegexpTitle(String regexp);
	public PostsPage getlAllPostsByPage(int lp, int size);
}