package com.bartosztanski.BlogApp.controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","http://localhost:3000"})
@RequestMapping("/")
public class HomeController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@GetMapping()
	public List<String> homePage() {
		LOGGER.info("Inside HomeController.home");
		List<String> endpointsList = new LinkedList<>();
		endpointsList.add("GET METHOD API LISTS:");
		endpointsList.add("https://blogbartosz.azurewebsites.net/api/v1/posts (GET) Returns list of all posts in database");
		endpointsList.add("https://blogbartosz.azurewebsites.net/api/v1/post/{postId} (GET) Returns single post with given id");
		endpointsList.add("https://blogbartosz.azurewebsites.net/api/v1/post/{postId}/comments (GET) Returns comments to post with given id");
		endpointsList.add("https://blogbartosz.azurewebsites.net/api/v1/video/stream/{videoId} (GET) Returns video with given id");
		endpointsList.add("https://blogbartosz.azurewebsites.net/api/v1/video/stream/{id} (GET) Returs video stream");
		return endpointsList;
	}
}
