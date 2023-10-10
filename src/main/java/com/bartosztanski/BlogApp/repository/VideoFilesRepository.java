package com.bartosztanski.BlogApp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bartosztanski.BlogApp.entity.VideoFilesEntity;

public interface VideoFilesRepository extends MongoRepository<VideoFilesEntity, String> {
	
	List<VideoFilesEntity> findAll();
}
