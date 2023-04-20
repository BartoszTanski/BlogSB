package com.bartosztanski.BlogApp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bartosztanski.BlogApp.entity.VideoEntity;
import com.bartosztanski.BlogApp.service.VideoService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","https://bartosztanski.azurewebsites.net","http://localhost:3000"})
@RequestMapping("/api/v1/video")
public class VideoController {
	
	private final VideoService videoService;
	
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}
	
	private final Logger LOGGER = LoggerFactory.getLogger(VideoController.class);
	
	@GetMapping("/{id}")
	public ResponseEntity<VideoEntity> getVideoById(@PathVariable("id") String id) throws IllegalStateException, IOException {
		LOGGER.info("Inside PostController.getVideoById");
		VideoEntity video = videoService.getVideo(id);
		return ResponseEntity.ok(video);
	}
	@PostMapping("/")
	public ResponseEntity<String> addVideo(@RequestParam("video") MultipartFile file) throws IOException {
		LOGGER.info("Inside PostController.addVideo");
		String id = videoService.addVideo(file);
		return ResponseEntity.ok(id);
	}
	@GetMapping("/stream/{id}")
	public void streamVideo(@PathVariable String id, HttpServletResponse response) throws Exception {
	    VideoEntity video = videoService.getVideo(id);
	    FileCopyUtils.copy(video.getStream(), response.getOutputStream());        
	}
}
