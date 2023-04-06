package com.bartosztanski.BlogApp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bartosztanski.BlogApp.entity.PostEntity;
import com.bartosztanski.BlogApp.error.PostNotFoundExcepction;
import com.bartosztanski.BlogApp.model.PostRequest;
import com.bartosztanski.BlogApp.model.PostResponse;

@Service
public interface PostService {
	public String addPost(PostRequest postRequest); 
	public void updatePost(String id, PostRequest postRequest); 
	public List<PostEntity> getlAllPosts();
	public List<PostEntity> getPostsByDate(LocalDate date); 
	public void deleteById(String id);
	public PostResponse getPostById(String id) throws PostNotFoundExcepction;
	public byte[] getImage(String id); 
}